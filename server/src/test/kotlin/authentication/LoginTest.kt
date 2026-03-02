package authentication

import MockDatabase
import database.tables.UserTable
import exceptions.LoginException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import script.UserManager
import server.JSONModels.LoginJson
import server.JSONModels.RegisterJson

class LoginTest {
    val dbConnection = MockDatabase().setupDatabase()

    @BeforeEach
    fun `Create User Tables`() {
        transaction(dbConnection) {
            SchemaUtils.create(UserTable)
            UserTable.insert {
                it[name] = "Test"
                it[surname] = "Test"
                it[email] = "test@gmail.com"
                it[password] = "12345678"
                it[verified] = false
                it[role] = "user"
            }
        }
    }

    @AfterEach
    fun `Remove all Users`() {
        transaction(dbConnection) {
            UserTable.deleteAll()
        }
    }

    @Test
    fun `Existing User Login Test`(){
        val userManager = UserManager(dbConnection)
        val loginJson = LoginJson(
            email = "test@gmail.com",
            password = "12345678",
        )

        try {
            userManager.login(loginJson)
        }catch (e: LoginException){
            Assertions.assertTrue(false)
        }
    }

    @Test
    fun `Not Valid Login Test`(){
        val userManager = UserManager(dbConnection)
        val loginJson = LoginJson(
            email = "test@gmail.com",
            password = "1234567",
        )

        try {
            if(userManager.login(loginJson) == -1){
                throw LoginException("Invalid login.")
            }
            Assertions.assertTrue(false)
        }catch (e: LoginException){
            Assertions.assertTrue(true)
        }
    }

    @Test
    fun `Empty Login Test`(){
        val userManager = UserManager(dbConnection)
        val loginJson = LoginJson(
            email = "",
            password = "",
        )

        try {
            userManager.login(loginJson)
            Assertions.assertTrue(false)
        }catch (e: LoginException){
            Assertions.assertTrue(true)
        }
    }

    @Test
    fun `Single Empty Login Test`(){
        val userManager = UserManager(dbConnection)
        val loginJson = LoginJson(
            email = "",
            password = "12345678",
        )

        try {
            userManager.login(loginJson)
            Assertions.assertTrue(false)
        }catch (e: LoginException){
            Assertions.assertTrue(true)
        }
    }
}