package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object NewsTable:  IntIdTable("News")  {
    val title = varchar("title", 100)
    val content = text("content")
    val imagePath = varchar("imagePath", 100)
    val language = varchar("language", 2)
    val uploadedBy = integer("uploadedBy").references(UserTable.id)
    val author = varchar("author", 100)
}