package server.JSONModels

import kotlinx.serialization.Serializable

@Serializable
class LicenceJson(
    val licenceNumber: String,
    val releasedBy: String,
    val season: String,
    val noKill: Boolean,
    val type: String,
    val qrCodeToken: String,
    val userId: Int
) {
    override fun toString(): String {
        return """
{
    "licenceNumber": "$licenceNumber",
    "releasedBy": "$releasedBy",
    "season": "$season",
    "noKill": $noKill,
    "type": "$type",
    "qrCodeToken": "$qrCodeToken",
    "userId": $userId
}
        """.trimIndent()
    }
}