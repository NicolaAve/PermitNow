package script

import database.tables.NewsTable
import exceptions.NewsException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import server.JSONModels.NewsJson

class NewsManager(val connection: Database) {
    fun getAllNews(): List<NewsJson>{
        try {
            return transaction(connection) {
                NewsTable.selectAll().map {
                    NewsJson(
                        it[NewsTable.title],
                        it[NewsTable.content],
                        it[NewsTable.language],
                        it[NewsTable.uploadedBy],
                        it[NewsTable.author]
                    )
                }
            }
        }catch (e: Exception){
            throw NewsException("Error during News retrieve: ${e.message}")
        }
    }
}