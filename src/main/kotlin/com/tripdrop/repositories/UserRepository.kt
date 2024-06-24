package com.tripdrop.repositories

import com.tripdrop.models.User
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserRepository(private val db: CoroutineDatabase) {
    private val users = db.getCollection<User>()

    suspend fun addUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

    suspend fun getUserById(id: ObjectId): User? {
        return users.findOneById(id)
    }
}
