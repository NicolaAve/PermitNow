package utils

import java.util.UUID

object CommonFunctions {
    fun generateQRCodeToken(): String{
        return UUID.randomUUID().toString()
    }
}