package com.tripdrop.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Transaction(
    @BsonId val id: ObjectId = ObjectId(),
    val amount: Double,
    val fromUserId: ObjectId,
    val toUserId: ObjectId,
    val deliveryRequestId: ObjectId,
    val status: String = "pending",
    val createdAt: Long = System.currentTimeMillis()
)
