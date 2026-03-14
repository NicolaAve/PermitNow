package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object FishingPermitsTable:  IntIdTable("FishingPermits")  {
    val type = varchar("type", 100) // DAILY or ANNUAL
    val expirationDate = date("expiration_date")
    val qrCodeToken = varchar("qr_code_token", 100)
    val fishingLimit = integer("fishing_limit")
    val userId = integer("userId").references(UserTable.id)
}