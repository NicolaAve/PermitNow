package database.dao

import database.tables.NewsTable
import database.tables.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class NewsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewsEntity>(NewsTable)
    var title by NewsTable.title
    var content by NewsTable.content
    var imagePath by NewsTable.imagePath
    var language by NewsTable.language
    var uploadedBy by NewsTable.uploadedBy
    var author by NewsTable.author
}