package database.dao

import database.tables.FishingLicencesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FishingLicencesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FishingLicencesEntity>(FishingLicencesTable)

    var qr_code_token by FishingLicencesTable.qr_code_token
    var userId by FishingLicencesTable.userId
    var status by FishingLicencesTable.status
    var licenceNumber by FishingLicencesTable.licenceNumber
    var releasedBy by FishingLicencesTable.releasedBy
    var season by FishingLicencesTable.season
    var noKill by FishingLicencesTable.noKill

}