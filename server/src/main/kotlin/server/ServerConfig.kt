package server

import configuration.ReadXMLResources
import exceptions.DecryptionException
import exceptions.LoginException
import exceptions.RegistrationException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
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
import script.CipherLogic
import script.FishingLicencesManager
import script.GoogleVision
import server.JSONModels.FishingLicenceJson
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
val fishingLicenceManager = FishingLicencesManager(connection)
val cipherLogic = CipherLogic(permitNowConfiguration.cipherConfiguration!!)


fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            allowTrailingComma = true
        })
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)

        anyHost() // da togliere in produzione
    }




    routing {
        get("/status"){
            call.respondText("Server is running: ${LocalDateTime.now()}")
        }

        post("/register"){
            val input = call.receive<RegisterJson>()
            try {
//                val decryptedJson = RegisterJson(
//                    name = cipherLogic.decrypt(input.name),
//                    surname = cipherLogic.decrypt(input.surname),
//                    email = cipherLogic.decrypt(input.email),
//                    password = cipherLogic.decrypt(input.password),
//                    role = cipherLogic.decrypt(input.role)
//                )
                userManager.register(input)
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
//                val decryptedJson = LoginJson(
//                    email = cipherLogic.decrypt(input.email),
//                    password = cipherLogic.decrypt(input.password)
//                )

                userId = userManager.login(input)

                if(userId != -1){
                    println("Login success, user ${input.email} found")
                    call.respond(userId)
                }else{
                    println("Login failed, user ${input.email} not found")
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

        post("/login/admin"){
            val input = call.receive<LoginJson>()
            var userId: Int
            try{
//                val decryptedJson = LoginJson(
//                    email = cipherLogic.decrypt(input.email),
//                    password = cipherLogic.decrypt(input.password)
//                )

                userId = userManager.adminLogin(input)

                if(userId != -1){
                    println("Login success, user ${input.email} found")
                    call.respond(userId)
                }else{
                    println("Login failed, user ${input.email} not found")
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

        post("/licence/verify/fishing"){
            val input = call.receive<FishingLicenceJson>()
            try{
                fishingLicenceManager.createNewLicenceRequest(input.licenceText, input.userId)
                call.respondText("success")
            }catch (e: Exception){
                println("Error during licence creation: ${e.stackTraceToString()}")
                call.respondText("failure")
            }

        }


        post("/licence/analyze/fishing"){
            val multipart = call.receiveMultipart()
            var imageBytes: ByteArray? = null

            val textParameters = mutableMapOf<String, String>()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        imageBytes = part.streamProvider().readBytes()
                    }
                    is PartData.FormItem -> {
                        val paramName = part.name
                        val paramValue = part.value

                        if (paramName != null) {
                            textParameters[paramName] = paramValue
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            try {
                val response = GoogleVision.extractTextFromImage(imageBytes!!)
                println(response)
                call.respondText(response)
            }catch (e: Exception){
                println("Error during licence analysis: ${e.stackTraceToString()}")
                call.respondText("failure")
            }

        }

    }
}

object ServerConfig {
    fun run() {
        embeddedServer(
            Netty,
            port = serverConfiguration!!.port.toInt(),
            host = "127.0.0.1",
            module = Application::module
        ).start(wait = true)
    }
}