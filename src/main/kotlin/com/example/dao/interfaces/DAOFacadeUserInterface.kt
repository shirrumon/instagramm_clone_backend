package com.example.dao.interfaces

import com.example.models.UserModel

interface DAOFacadeUserInterface {
    suspend fun allUsers(): List<UserModel>
    suspend fun getUserById(id: Int): UserModel?

    suspend fun createUser(
        name: String,
        email: String,
        password: String,
        profileImage: String?
    ): UserModel?

    suspend fun deleteUserById(id: Int): Boolean

    suspend fun editUser(
        id: Int,
        name: String,
        profileImage: String
    ): Boolean

    suspend fun changeUserPassword(
        id: Int,
        password: String
    ): Boolean
}