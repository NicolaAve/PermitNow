package server.JSONModels

import kotlinx.serialization.Serializable

@Serializable
class NewPermitJson(
    val type: String,
    val userId: Int,
    val limit: Int,
) {
    override fun toString(): String {
        return """
{
    "type": "$type",
    "userId": $userId,
    "limit": $limit
}
        """.trimIndent()
    }
}