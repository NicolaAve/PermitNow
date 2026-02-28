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