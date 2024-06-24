package com.tripdrop.routes

import com.tripdrop.models.User
import com.tripdrop.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.mindrot.jbcrypt.BCrypt

fun Route.userRoutes(userRepository: UserRepository) {
    authenticate {
        route("/users") {
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val user = userRepository.getUserById(ObjectId(id))
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }

            put("/{id}") {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updateUser = call.receive<User>()
                val existingUser = userRepository.getUserById(ObjectId(id))
                if (existingUser != null) {
                    val updatedUser = existingUser.copy(
                        name = updateUser.name,
                        email = updateUser.email,
                        password = BCrypt.hashpw(updateUser.password, BCrypt.gensalt())
                    )
                    userRepository.addUser(updatedUser)
                    call.respond(HttpStatusCode.OK, updatedUser)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }
        }
    }
}
