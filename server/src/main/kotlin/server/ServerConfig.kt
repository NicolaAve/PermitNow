package server

import configuration.ReadXMLResources
import exceptions.DecryptionException
import exceptions.LoginException
import exceptions.RegistrationException
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.port
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import script.UserManager
import server.JSONModels.LoginJson
import server.JSONModels.RegisterJson
import utils.CipherLogic
import java.time.LocalDateTime



val permitNowConfiguration = ReadXMLResources.getConfiguration()

val databaseConfiguration = permitNowConfiguration.database

val serverConfiguration = permitNowConfiguration.serverConfiguration

val connection = Database.connect(
    url = databaseConfiguration!!.dbConnectionString,
    user = databaseConfiguration.dbUser,
    password = databaseConfiguration.dbPassword,
    driver = databaseConfiguration.dbDriver
)

val userManager = UserManager(connection)
val cipherLogic = CipherLogic(permitNowConfiguration.cipherConfiguration!!)


fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            allowTrailingComma = true
        })
    }


    routing {
        get("/status"){
            call.respondText("Server is running: ${LocalDateTime.now()}")
        }

        post("/register"){
            val input = call.receive<RegisterJson>()
            try {
                val decryptedJson = RegisterJson(
                    name = cipherLogic.decrypt(input.name),
                    surname = cipherLogic.decrypt(input.surname),
                    email = cipherLogic.decrypt(input.email),
                    password = cipherLogic.decrypt(input.password),
                    role = cipherLogic.decrypt(input.role)
                )
                userManager.register(decryptedJson)
                call.respondText("success")
            }catch (e: RegistrationException){
                println(e.message)
                call.respondText("failure")
            }catch(e: DecryptionException){
                println(e.message)
                call.respondText("failure")
            }catch (e: Exception){
                println("Error during registration: ${e.stackTraceToString()}")
                call.respondText("failure")
            }
        }

        post("/login"){
            val input = call.receive<LoginJson>()
            var userId: Int
            try{
                val decryptedJson = LoginJson(
                    email = cipherLogic.decrypt(input.email),
                    password = cipherLogic.decrypt(input.password)
                )

                userId = userManager.login(decryptedJson)

                if(userId != -1){
                    println("Login success, user ${decryptedJson.email} found")
                    call.respond(userId)
                }else{
                    println("Login failed, user ${decryptedJson.email} not found")
                    call.respond(-1)
                }

            }catch (e: DecryptionException){
                println(e.message)
                call.respond(-1)
            }catch (e: LoginException){
                println(e.message)
                call.respond(-1)
            }catch (e: Exception){
                println("Error during login: ${e.stackTraceToString()}")
                call.respond(-1)
            }
        }

    }
}

object ServerConfig {
    fun run() {
        embeddedServer(
            Netty,
            port = serverConfiguration!!.port.toInt(),
            host = "0.0.0.0",
            module = Application::module
        ).start(wait = true)
    }
}