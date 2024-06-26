package com.tripdrop

import com.tripdrop.models.DeliveryRequest
import com.tripdrop.models.Transaction
import com.tripdrop.models.User
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object Database {

    val mongopw = System.getenv("MONGO_PW")

    private val client = KMongo.createClient(
        connectionString = "mongodb+srv://krishna:$mongopw@cluster0.hfyezac.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    ).coroutine
    val database: CoroutineDatabase = client.getDatabase("tripdrop")

    init {
        runBlocking {
            createIndexes()
        }
    }

    private suspend fun createIndexes() {
        // Create index for email in User collection
        database.getCollection<User>().ensureIndex("{ email: 1 }")

        // Create indexes for other collections if needed
        database.getCollection<DeliveryRequest>().ensureIndex("{ from: 1 }")
        database.getCollection<DeliveryRequest>().ensureIndex("{ to: 1 }")
        database.getCollection<Transaction>().ensureIndex("{ fromUserId: 1 }")
        database.getCollection<Transaction>().ensureIndex("{ toUserId: 1 }")
    }
}
