package exceptions

class RegistrationException(customMessage: String): Exception() {
    override val message: String = "Error During Registration: $customMessage"
}