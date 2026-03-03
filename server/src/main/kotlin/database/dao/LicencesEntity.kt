package database.dao

import database.tables.LicencesTable
import database.tables.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LicencesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LicencesEntity>(LicencesTable)

    var qr_code_token by LicencesTable.qr_code_token
    var userId by LicencesTable.userId

}