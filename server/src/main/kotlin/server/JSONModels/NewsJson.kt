package server.JSONModels

import kotlinx.serialization.Serializable

@Serializable
class NewsJson (
    val title: String,
    val content: String,
    val language: String,
    val uploadedBy: Int,
    val author: String,
){
    override fun toString(): String {
        return """
{
    "title": "$title",
    "content": "$content",
    "language": "$language",
    "uploadedBy": $uploadedBy,
    "author": "$author"
}
        """.trimIndent()
    }
}
