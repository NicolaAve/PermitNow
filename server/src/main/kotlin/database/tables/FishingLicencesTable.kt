package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object FishingLicencesTable:  IntIdTable("FishingLicences")  {
    val qr_code_token = varchar("qr_code_token", 100)
    val userId = integer("userId").references(UserTable.id)
    val status = varchar("status", 100)
    val licenceNumber = varchar("licence_number", 100)
    val releasedBy = varchar("released_by", 100)
    val typeOfPermit = varchar("type_of_permit", 100)
    val season = varchar("season", 100)
    val noKill = bool("no_kill")
}