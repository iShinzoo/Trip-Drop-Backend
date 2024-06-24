package com.tripdrop.repositories

import com.tripdrop.models.Transaction
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class TransactionRepository(private val db: CoroutineDatabase) {
    private val transactions = db.getCollection<Transaction>()

    suspend fun addTransaction(transaction: Transaction): Boolean {
        return transactions.insertOne(transaction).wasAcknowledged()
    }

    suspend fun getTransactionsByUserId(userId: ObjectId): List<Transaction> {
        return transactions.find(Transaction::fromUserId eq userId).toList()
    }
}
