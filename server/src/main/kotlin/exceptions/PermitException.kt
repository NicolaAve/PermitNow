package exceptions

class PermitException(customMessage: String): Exception() {
    override val message: String = "Error During Permit Management: $customMessage"
}