import org.jetbrains.exposed.sql.Database

class MockDatabase {
    internal fun setupDatabase(): Database {

        val jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"

        val database = Database.Companion.connect(
            url = jdbcUrl,
            user = "root",
            password = "password",
            driver = "org.h2.Driver"
        )
        return database
    }
}