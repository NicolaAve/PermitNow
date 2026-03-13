package server.JSONModels

import kotlinx.serialization.Serializable

@Serializable
class UserJson (
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val role: String = "user",
    val fiscalCode: String = "",
    val verified: Boolean = false
){
    override fun toString(): String {
        return """
{
    "name": "$name",
    "surname": "$surname",
    "email": "$email",
    "password": "$password",
    "role": "$role",
    "fiscalCode": "$fiscalCode",
    "verified": $verified
}
        """.trimIndent()
    }
}