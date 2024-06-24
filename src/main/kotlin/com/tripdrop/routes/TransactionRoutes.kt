package com.example.routes

import com.tripdrop.models.Transaction
import com.tripdrop.repositories.TransactionRepository
import com.tripdrop.services.PaymentService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.transactionRoutes(transactionRepository: TransactionRepository) {
    val paymentService = PaymentService(transactionRepository)

    authenticate {
        route("/transactions") {
            post {
                val transaction = call.receive<Transaction>()
                val wasAcknowledged = paymentService.processPayment(
                    transaction.amount,
                    transaction.fromUserId,
                    transaction.toUserId,
                    transaction.deliveryRequestId
                )
                if (wasAcknowledged) {
                    call.respond(HttpStatusCode.Created, transaction)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Transaction creation failed")
                }
            }

            get("/{userId}") {
                val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val transactions = transactionRepository.getTransactionsByUserId(ObjectId(userId))
                call.respond(HttpStatusCode.OK, transactions)
            }
        }
    }
}
