package com.tripdrop.routes

import com.tripdrop.models.DeliveryRequest
import com.tripdrop.repositories.DeliveryRequestRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.deliveryRequestRoutes(deliveryRequestRepository: DeliveryRequestRepository) {
    authenticate {
        route("/requests") {
            post {
                val request = call.receive<DeliveryRequest>()
                val wasAcknowledged = deliveryRequestRepository.addRequest(request)
                if (wasAcknowledged) {
                    call.respond(HttpStatusCode.Created, request)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Request creation failed")
                }
            }

            get {
                val requests = deliveryRequestRepository.getAllRequests()
                call.respond(HttpStatusCode.OK, requests)
            }

            put("/{id}") {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val status = call.receive<Map<String, String>>()["status"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val wasAcknowledged = deliveryRequestRepository.updateRequestStatus(ObjectId(id), status)
                if (wasAcknowledged) {
                    call.respond(HttpStatusCode.OK, "Request updated")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Request not found")
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val wasAcknowledged = deliveryRequestRepository.deleteRequest(ObjectId(id))
                if (wasAcknowledged) {
                    call.respond(HttpStatusCode.OK, "Request deleted")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Request not found")
                }
            }
        }
    }
}
