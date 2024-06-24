package com.tripdrop.services

import com.tripdrop.models.Transaction
import com.tripdrop.repositories.TransactionRepository
import org.bson.types.ObjectId

class PaymentService(private val transactionRepository: TransactionRepository) {
    suspend fun processPayment(amount: Double, fromUserId: ObjectId, toUserId: ObjectId, deliveryRequestId: ObjectId): Boolean {
        val feePercentage = 0.10
        val fee = amount * feePercentage
        val netAmount = amount - fee

        val platformTransaction = Transaction(
            amount = fee,
            fromUserId = fromUserId,
            toUserId = ObjectId("platform-user-id"), // Platform's user ID
            deliveryRequestId = deliveryRequestId
        )
        val userTransaction = Transaction(
            amount = netAmount,
            fromUserId = fromUserId,
            toUserId = toUserId,
            deliveryRequestId = deliveryRequestId
        )

        return transactionRepository.addTransaction(platformTransaction) && transactionRepository.addTransaction(userTransaction)
    }
}
