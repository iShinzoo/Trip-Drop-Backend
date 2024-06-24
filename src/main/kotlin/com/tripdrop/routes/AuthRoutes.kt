package com.tripdrop.routes

import com.tripdrop.models.User
import com.tripdrop.repositories.UserRepository
import com.tripdrop.services.generateToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt

fun Route.authRoutes(userRepository: UserRepository) {
    route("/auth") {
        post("/register") {
            val user = call.receive<User>()
            val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
            val newUser = user.copy(password = hashedPassword)

            val wasAcknowledged = userRepository.addUser(newUser)
            if (wasAcknowledged) {
                call.respond(HttpStatusCode.Created, newUser)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "User registration failed")
            }
        }

        post("/login") {
            val loginRequest = call.receive<LoginRequest>()
            val user = userRepository.getUserByEmail(loginRequest.email)

            if (user == null || !BCrypt.checkpw(loginRequest.password, user.password)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }

            val token = generateToken(user.id.toString())
            call.respond(HttpStatusCode.OK, mapOf("token" to token))
        }
    }
}

data class LoginRequest(val email: String, val password: String)
