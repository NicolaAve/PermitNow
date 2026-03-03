package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object LicencesTable:  IntIdTable("Licences")  {
    val qr_code_token = varchar("qr_code_token", 100)
    val userId = integer("userId").references(UserTable.id)
}