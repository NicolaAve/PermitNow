package exceptions

class NewsException(customMessage: String): Exception() {
    override val message: String = "Error During News Management: $customMessage"
}