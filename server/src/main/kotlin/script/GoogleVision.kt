package script

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.ImageAnnotatorSettings
import com.google.protobuf.ByteString
import exceptions.ImageReadingException
import org.jetbrains.exposed.sql.Database
import utils.googleVisionKeyPath
import java.io.File
import java.io.FileInputStream

object GoogleVision{
    val connectionJsonPath = if(File("/home/ubuntu/etc/PermitNow/google-vision-key").exists()) {
        "/home/ubuntu/etc/PermitNow/google-vision-key"
    }else{
        googleVisionKeyPath
    }

    private fun visionClient(): ImageAnnotatorClient {
        val credentials = GoogleCredentials.fromStream(FileInputStream(connectionJsonPath))
        val settings = ImageAnnotatorSettings.newBuilder()
            .setCredentialsProvider { credentials }
            .build()

        return ImageAnnotatorClient.create(settings)
    }

    fun extractTextFromImage(imageBytes: ByteArray): String{
        val client = visionClient()
        var extractedText = ""

        val image = Image.newBuilder().setContent(ByteString.copyFrom(imageBytes)).build()

        val feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder()
            .addFeatures(feature)
            .setImage(image)
            .build()

        val requests = listOf(request)

        try {
            client.use { client ->
                val response = client.batchAnnotateImages(requests)

                for (res in response.responsesList) {
                    if (res.hasError()) {
                        throw ImageReadingException("Response includes error: ${res.error.message}")
                    }
                    extractedText =  res.textAnnotationsList.firstOrNull()?.description ?: ""
                }
            }

            return extractedText
        } catch (e: Exception) {
            throw ImageReadingException(e.message ?: "Unknown error occurred")
        }
    }


}