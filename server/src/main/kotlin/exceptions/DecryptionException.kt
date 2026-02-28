package exceptions

class DecryptionException(customMessage: String): Exception() {
    override val message: String = "Error During Decryption: $customMessage"
}