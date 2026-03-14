package database.dao

import database.tables.FishingPermitsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FishingPermitsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FishingPermitsEntity>(FishingPermitsTable)

    var type by FishingPermitsTable.type
    var expirationDate by FishingPermitsTable.expirationDate
    var qr_code_token by FishingPermitsTable.qrCodeToken
    var userId by FishingPermitsTable.userId
    var fishingLimit by FishingPermitsTable.fishingLimit
}