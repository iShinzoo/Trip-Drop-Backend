package com.tripdrop

import com.example.routes.transactionRoutes
import com.tripdrop.plugins.*
import com.tripdrop.repositories.DeliveryRequestRepository
import com.tripdrop.repositories.TransactionRepository
import com.tripdrop.repositories.UserRepository
import com.tripdrop.routes.authRoutes
import com.tripdrop.routes.deliveryRequestRoutes
import com.tripdrop.routes.userRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()

    val userRepository = UserRepository(Database.database)
    val deliveryRequestRepository = DeliveryRequestRepository(Database.database)
    val transactionRepository = TransactionRepository(Database.database)

    routing {
        get("/") {
            call.respondText("Trip Drop Backend", ContentType.Text.Plain)
        }
        authRoutes(userRepository)
        authenticate {
            userRoutes(userRepository)
            deliveryRequestRoutes(deliveryRequestRepository)
            transactionRoutes(transactionRepository)
        }
    }
}
