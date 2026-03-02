package authentication

import MockDatabase
import database.tables.UserTable
import exceptions.RegistrationException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import script.UserManager
import server.JSONModels.RegisterJson


class RegistrationTest {
    val dbConnection = MockDatabase().setupDatabase()

    @BeforeEach
    fun `Create User Tables`() {
        transaction(dbConnection) {
            SchemaUtils.create(UserTable)
        }
    }

    @AfterEach
    fun `Remove all Users`() {
        transaction(dbConnection) {
            UserTable.deleteAll()
        }
    }

    @Test
    fun `Valid Registration Test`(){
        val userManager = UserManager(dbConnection)
        val registerJson = RegisterJson(
            name = "Test",
            surname = "Test",
            email = "test@gmail.com",
            password = "12345678",
            role = "user"
        )

        try {
            userManager.register(registerJson)
            Assertions.assertTrue(true)
        }catch (e: Exception){
            Assertions.assertTrue(false)
        }
    }

    @Test
    fun `Empty Registration Test`(){
        val userManager = UserManager(dbConnection)
        val registerJson = RegisterJson(
            name = "",
            surname = "",
            email = "",
            password = "",
            role = ""
        )

        try {
            userManager.register(registerJson)
            Assertions.assertTrue(false)
        }catch (e: RegistrationException){
            Assertions.assertTrue(true)
        }catch (e: Exception){
            Assertions.assertTrue(false)
        }
    }

    @Test
    fun `Single Empty Parameter Registration Test`(){
        val userManager = UserManager(dbConnection)
        val registerJson = RegisterJson(
            name = "test",
            surname = "test",
            email = "",
            password = "12345678",
            role = "user"
        )

        try {
            userManager.register(registerJson)
            Assertions.assertTrue(false)
        }catch (e: RegistrationException){
            Assertions.assertTrue(true)
        }catch (e: Exception){
            Assertions.assertTrue(false)
        }
    }

    @Test
    fun `Not valid Email Registration Test`(){
        val userManager = UserManager(dbConnection)
        val registerJson = RegisterJson(
            name = "test",
            surname = "test",
            email = "test",
            password = "12345678",
            role = "user"
        )

        try {
            userManager.register(registerJson)
            Assertions.assertTrue(false)
        }catch (e: RegistrationException){
            Assertions.assertTrue(true)
        }catch (e: Exception){
            Assertions.assertTrue(false)
        }
    }

    @Test
    fun `Not valid Role Registration Test`(){
        val userManager = UserManager(dbConnection)
        val registerJson = RegisterJson(
            name = "test",
            surname = "test",
            email = "test@gmail,com",
            password = "12345678",
            role = "test"
        )

        try {
            userManager.register(registerJson)
            Assertions.assertTrue(false)
        }catch (e: RegistrationException){
            Assertions.assertTrue(true)
        }catch (e: Exception){
            Assertions.assertTrue(false)
        }
    }





}