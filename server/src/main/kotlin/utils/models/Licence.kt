package utils.models

data class Licence(
    val licenceNumber: String,
    val releasedBy: String,
    val typeOfPermit: String,
    val season: String,
    val noKill: Boolean,
    val type: String,
    val qrCodeToken: String,
    val userId: Int
)