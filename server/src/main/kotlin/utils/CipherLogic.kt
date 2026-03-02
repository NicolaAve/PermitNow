package utils

import configuration.CipherConfiguration
import exceptions.DecryptionException
import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "AES"
private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"

class CipherLogic(val cipherConfiguration: CipherConfiguration) {


    fun decrypt(encryptedText: String): String {
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)

            val keyBytes = cipherConfiguration.key.chunked(2)
                .map { it.toInt(16).toByte() }
                .toByteArray()

            val secretKeySpec = SecretKeySpec(keyBytes, ALGORITHM)

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val decodedBytes = Base64.getDecoder().decode(encryptedText)
            val decryptedBytes = cipher.doFinal(decodedBytes)

            return String(decryptedBytes, Charsets.UTF_8)

        } catch (e: Exception) {
            throw DecryptionException("Error during decryption: ${e.message}")
        }
    }

    fun encrypt(plainText: String): String {
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec())

            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            // Codifica l'array di byte crittografato in una stringa Base64
            return Base64.getEncoder().encodeToString(encryptedBytes)

        } catch (e: Exception) {
            throw RuntimeException("Errore durante l'encryption di test: ${e.message}", e)
        }
    }

    private fun getSecretKeySpec(): SecretKeySpec {
        val keyBytes = cipherConfiguration.key.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
        return SecretKeySpec(keyBytes, ALGORITHM)
    }
}