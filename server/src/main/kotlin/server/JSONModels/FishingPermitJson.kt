package server.JSONModels

import kotlinx.serialization.Serializable

@Serializable
class FishingPermitJson(
    val type: String,
    val expireDate: String,
    val qrCodeToken: String,
    val userId: Int? = null,
) {
    override fun toString(): String {
        return """
{
    "type": "$type",
    "expireDate": "$expireDate",
    "qrCodeToken": "$qrCodeToken",
    "userId": $userId
}
        """.trimIndent()
    }
}