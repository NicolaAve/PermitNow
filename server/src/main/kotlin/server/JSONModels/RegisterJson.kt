package server.JSONModels

import kotlinx.serialization.Serializable

@Serializable
class RegisterJson (
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val role: String = "user"
){
    override fun toString(): String {
        return """
{
    "name": "$name",
    "surname": "$surname",
    "email": "$email",
    "password": "$password",
    "role": "$role"
}
        """.trimIndent()
    }
}