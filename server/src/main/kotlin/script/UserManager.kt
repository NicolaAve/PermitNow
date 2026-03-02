package script

import database.tables.UserTable
import exceptions.LoginException
import exceptions.RegistrationException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Intersect
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import server.JSONModels.LoginJson
import server.JSONModels.RegisterJson
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll

class UserManager(val connection: Database) {
    fun register(registerJson: RegisterJson) {
        if(registerJson.role != "user" && registerJson.role != "admin") throw RegistrationException("Role not valid.")
        if(registerJson.name == "" || registerJson.surname == "" || registerJson.email == "" || registerJson.password == "") throw RegistrationException("Missing required fields.")
        if(!registerJson.email.contains("@")) throw RegistrationException("Email not valid.")

        try{
            transaction(connection) {
                UserTable.insert {
                    it[name] = registerJson.name
                    it[surname] = registerJson.surname
                    it[email] = registerJson.email
                    it[password] = registerJson.password
                    it[verified] = false
                    it[role] = registerJson.role
                }
            }
        }catch (e: Exception){
            throw RegistrationException(e.message!!)
        }
    }

    fun login(loginJson: LoginJson): Int {
        try{
            if(loginJson.email == "" || loginJson.password == "") throw LoginException("Missing required fields.")
            if(!loginJson.email.contains("@")) throw LoginException("Email not valid.")

            return transaction(connection){
                val userExists = UserTable.selectAll().where {
                    (UserTable.email eq loginJson.email) and (UserTable.password eq loginJson.password)
                }.count() > 0

                 if (userExists) {
                    UserTable.selectAll().where{
                        (UserTable.email eq loginJson.email) and (UserTable.password eq loginJson.password)
                    }.firstOrNull()!![UserTable.id].toString().toInt()
                }else{
                    -1
                }
            }

        }catch (e: Exception){
            throw LoginException("Error during Login: ${e.message}")
        }
    }
}