package script

import database.tables.FishingLicencesTable
import database.tables.UserTable
import exceptions.FishingLicenceException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import utils.models.Licence
import utils.models.User
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class FishingLicencesManager(val connection: Database) {
    fun createNewLicenceRequest(rowText: String, userId: Int) {
        val licence = getLicenceFromText(rowText, userId)

        try {
            transaction(connection) {
                FishingLicencesTable.insert {
                    it[FishingLicencesTable.licenceNumber] = licence.licenceNumber
                    it[FishingLicencesTable.releasedBy] = licence.releasedBy
                    it[FishingLicencesTable.typeOfPermit] = licence.typeOfPermit
                    it[FishingLicencesTable.userId] = userId
                    it[FishingLicencesTable.status] = "pending"
                    it[FishingLicencesTable.qr_code_token] = generateQRCodeToken()
                    it[FishingLicencesTable.season] = licence.season
                    it[FishingLicencesTable.noKill] = licence.noKill
                }
            }
        }catch (e: Exception){
            throw FishingLicenceException("Error creating licence request: ${e.message}")
        }
    }


    fun generateQRCodeToken(): String{
         return UUID.randomUUID().toString()
    }

    fun getLicenceFromText(retrievedText: String, userId: Int): Licence{

        val tmpMap = mutableMapOf<String, String>()

        val regexCF = Regex("""\b[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]\b""", RegexOption.IGNORE_CASE)

        val regexCognome = Regex("""Cognome[\s:]+([A-ZÀ-ÿ']+(?:\s+[A-ZÀ-ÿ']+)*?)(?=\s*(?:Nato|Nome|Codice|Il|Residenza|\n|\r|$))""", RegexOption.IGNORE_CASE)
        val regexNome = Regex("""Nome[\s:]+([A-ZÀ-ÿ']+(?:\s+[A-ZÀ-ÿ']+)*?)(?=\s*(?:Nato|Cognome|Codice|Il|Residenza|\n|\r|$))""", RegexOption.IGNORE_CASE)

        val regexStagione = Regex("""(?:stagione|anno|tesseramento)[\s:]*(20\d{2})\b""", RegexOption.IGNORE_CASE)

        val regexNumeroLicenza = Regex("""(?:Licenza|Tessera)[\sNn°.:]+([A-Z0-9\-]+)""", RegexOption.IGNORE_CASE)

        val regexEntePAT = Regex("""(Provincia\s+Autonoma\s+di\s+Trento)""", RegexOption.IGNORE_CASE)
        val regexEnteAssociazione = Regex("""(Associazione\s+Pescatori[A-ZÀ-ÿ\s]*?)(?=\s*(?:Libretto|Permesso|Tessera|Zona|Controllo|\n|\r|$))""", RegexOption.IGNORE_CASE)

        val regexTipo = Regex("""Tipo[\s:]*([A-Z]+)\b""", RegexOption.IGNORE_CASE)
        val regexNoKill = Regex("""No\s*[-]?\s*Kill\b""", RegexOption.IGNORE_CASE)


        tmpMap["CF"] = regexCF.find(retrievedText)?.value ?: ""
        tmpMap["surname"] = regexCognome.find(retrievedText)?.value!!.substringAfter("Cognome: ")
        tmpMap["name"] = regexNome.find(retrievedText)?.value!!.substringAfter("Nome: ")
        tmpMap["season"] = regexStagione.find(retrievedText)?.value ?: ""
        tmpMap["licenceNumber"] = regexNumeroLicenza.find(retrievedText)?.value!!.substringAfter("Tessera N.: ")
        tmpMap["releasedBy"] = regexEntePAT.find(retrievedText)?.value ?: ""
        tmpMap["releasedByAss"] = regexEnteAssociazione.find(retrievedText)?.value ?: ""
        tmpMap["type"] = regexTipo.find(retrievedText)?.value ?: ""
        tmpMap["noKill"] = regexNoKill.find(retrievedText)?.value ?: ""

        val user: User = transaction(connection) {
            UserTable.selectAll().where {
                UserTable.id eq userId
            }.map {
                User(
                    name = it[UserTable.name],
                    surname = it[UserTable.surname],
                    fiscalCode = it[UserTable.fiscalCode],
                    verified = it[UserTable.verified]
                )
            }.first()
        }


        try {
            if(isValid(user, tmpMap)){
                return Licence(
                    tmpMap["licenceNumber"]!!.replace(" ", ""),
                    tmpMap["releasedBy"]!!.replace(" ", "").ifEmpty { tmpMap["releasedByAss"]!!.replace(" ", "") },
                    tmpMap["type"]!!.replace(" ", ""),
                    tmpMap["season"]!!.replace(" ", ""),
                    (tmpMap["noKill"] == "No Kill" || tmpMap["noKill"] == "NoKill"),
                    tmpMap["type"]!!.replace(" ", ""),
                    generateQRCodeToken(),
                    userId
                )
            }else{
                throw FishingLicenceException("User not valid")
            }
        }catch (e: Exception){
            throw FishingLicenceException("Error getting licence from text: ${e.message}")
        }

    }


    private fun isValid(user: User, tmpMap: Map<String, String>): Boolean{
        println("User: $user")
        println("tmpMap: $tmpMap")
        return user.verified && user.name == tmpMap["name"]!!.replace(" ", "") && user.surname == tmpMap["surname"]!!.replace(" ", "") && user.fiscalCode == tmpMap["CF"]!!.replace(" ", "")
    }
}