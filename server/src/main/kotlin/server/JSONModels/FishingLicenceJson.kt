package server.JSONModels

import kotlinx.serialization.Serializable

@Serializable
class FishingLicenceJson(
    val licenceText: String,
    val userId: Int
)