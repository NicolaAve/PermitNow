package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable:  IntIdTable("Users")  {
    val name = varchar("name", 100)
    val surname = varchar("surname", 100)
    val password = varchar("password", 100)
    val email = varchar("email", 255)
    val verified = bool("verified")
    val role = varchar("role", 100)
}