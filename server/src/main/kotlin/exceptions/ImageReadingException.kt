package exceptions

class ImageReadingException(customMessage: String): Exception() {
    override val message: String = "Error During Image Parsing: $customMessage"
}