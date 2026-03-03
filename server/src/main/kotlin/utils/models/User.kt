package utils.models

data class User(
    val name: String,
    val surname: String,
    val fiscalCode: String,
    val verified: Boolean
){
    override fun toString(): String {
        return "User(name='$name', surname='$surname', fiscalCode='$fiscalCode', verified=$verified)"
    }
}