package com.thewyp.data.repository.user

import com.thewyp.data.models.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean

}