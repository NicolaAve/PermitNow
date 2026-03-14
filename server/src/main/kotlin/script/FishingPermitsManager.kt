package script

import database.tables.FishingPermitsTable
import exceptions.PermitException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import server.JSONModels.FishingPermitJson
import server.JSONModels.NewPermitJson
import utils.CommonFunctions
import utils.TypesOfFishingPermits
import java.time.LocalDate

class FishingPermitsManager(val connection: Database) {

    fun createNewPermit(newPermitJson: NewPermitJson): Int{
        try {
            var tmpExpirationDate: LocalDate
            var permitType: TypesOfFishingPermits
            when(newPermitJson.type){
                "daily" -> {
                    permitType = TypesOfFishingPermits.DAILY
                    tmpExpirationDate = LocalDate.now()
                }
                "annual" -> {
                    permitType = TypesOfFishingPermits.ANNUAL
                    tmpExpirationDate = LocalDate.now().plusYears(1)
                }
                else -> throw PermitException("Type of permit not valid.")
            }




            return transaction(connection){
                FishingPermitsTable.insert {
                    it[FishingPermitsTable.type] = permitType.toString()
                    it[FishingPermitsTable.userId] = newPermitJson.userId
                    it[FishingPermitsTable.fishingLimit] = newPermitJson.limit
                    it[FishingPermitsTable.expirationDate] = tmpExpirationDate
                    it[FishingPermitsTable.qrCodeToken] = CommonFunctions.generateQRCodeToken()
                }[FishingPermitsTable.id].toString().toInt()
            }
        }catch (e: Exception){
            throw PermitException("Error during Permit creation: ${e.message}")
        }
    }

    fun getPermitsForUser(userId: Int): List<FishingPermitJson>{
        try {
            return transaction(connection){
                FishingPermitsTable.selectAll().where{
                    FishingPermitsTable.id eq userId
                }.map {
                    FishingPermitJson(
                        it[FishingPermitsTable.type],
                        it[FishingPermitsTable.expirationDate].toString(),
                        it[FishingPermitsTable.qrCodeToken],
                        it[FishingPermitsTable.userId],
                    )
                }
            }
        }catch (e: Exception){
            throw PermitException("Error during Permit retrieve: ${e.message}")
        }
    }

    fun getAllPermits(): List<FishingPermitJson>{
        try {
            return transaction(connection){
                FishingPermitsTable.selectAll().map {
                    FishingPermitJson(
                        it[FishingPermitsTable.type],
                        it[FishingPermitsTable.expirationDate].toString(),
                        it[FishingPermitsTable.qrCodeToken],
                        it[FishingPermitsTable.userId],
                    )
                }
            }
        }catch (e: Exception){
            throw PermitException("Error during Permit retrieve: ${e.message}")
        }
    }

    fun getValidPermits(): List<FishingPermitJson>{
        try {
            return transaction(connection){
                FishingPermitsTable.selectAll().orderBy(
                    FishingPermitsTable.id, SortOrder.DESC
                ).where {
                    FishingPermitsTable.expirationDate greaterEq LocalDate.now()
                }.map {
                    FishingPermitJson(
                        it[FishingPermitsTable.type],
                        it[FishingPermitsTable.expirationDate].toString(),
                        it[FishingPermitsTable.qrCodeToken],
                        it[FishingPermitsTable.userId],
                    )
                }
            }
        }catch (e: Exception){
            throw PermitException("Error during Permit retrieve: ${e.message}")
        }
    }

}