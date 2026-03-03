package exceptions

class FishingLicenceException(customMessage: String): Exception() {
    override val message: String = "Error During Fishing Licence Management: $customMessage"
}