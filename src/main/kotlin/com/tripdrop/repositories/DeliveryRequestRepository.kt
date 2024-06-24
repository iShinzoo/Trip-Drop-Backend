package com.tripdrop.repositories

import com.tripdrop.models.DeliveryRequest
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.setValue

class DeliveryRequestRepository(private val db: CoroutineDatabase) {
    private val requests = db.getCollection<DeliveryRequest>()

    suspend fun addRequest(request: DeliveryRequest): Boolean {
        return requests.insertOne(request).wasAcknowledged()
    }

    suspend fun getAllRequests(): List<DeliveryRequest> {
        return requests.find().toList()
    }

    suspend fun updateRequestStatus(id: ObjectId, status: String): Boolean {
        val result = requests.updateOneById(id, setValue(DeliveryRequest::status, status))
        return result.wasAcknowledged()
    }

    suspend fun deleteRequest(id: ObjectId): Boolean {
        val result = requests.deleteOneById(id)
        return result.wasAcknowledged()
    }
}
