package exceptions

class LoginException(customMessage: String): Exception() {
    override val message: String = "Error During Login: $customMessage"
}