package com.tripdrop.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class DeliveryRequest(
    @BsonId val id: ObjectId = ObjectId(),
    val from: String,
    val to: String,
    val description: String,
    val reward: Double,
    val requesterId: ObjectId,
    val delivererId: ObjectId? = null,
    val status: String = "pending",
    val createdAt: Long = System.currentTimeMillis()
)
