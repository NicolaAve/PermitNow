package authentication

import configuration.ReadXMLResources
import exceptions.DecryptionException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import script.CipherLogic


class EncryptionTest {
    val permitNowConfiguration = ReadXMLResources.getConfiguration()
    val cipherLogic = CipherLogic(permitNowConfiguration.cipherConfiguration!!)
    val encryptedMessage = cipherLogic.encrypt("Hello World")

    @Test
    fun `Valid Encryption Test`(){
        try{
            val decryptedMessage = cipherLogic.decrypt(encryptedMessage)
            Assertions.assertTrue(decryptedMessage == "Hello World")
        }catch (e: DecryptionException){
            println("Exception of Decryption")
            Assertions.assertTrue(false)
        }
    }

    @Test
    fun `Invalid Encryption Test`(){
        try{
            val decryptedMessage = cipherLogic.decrypt(encryptedMessage)
            Assertions.assertFalse(decryptedMessage == "Hello")
        }catch (e: DecryptionException){
            println("Exception of Decryption")
            Assertions.assertTrue(true)
        }
    }

}