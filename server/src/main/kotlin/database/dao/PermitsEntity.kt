package database.dao

import database.tables.PermitsTable
import database.tables.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PermitsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PermitsEntity>(PermitsTable)

    var type by PermitsTable.type
    var expirationDate by PermitsTable.expirationDate
    var qr_code_token by PermitsTable.qr_code_token
    var userId by PermitsTable.userId
}