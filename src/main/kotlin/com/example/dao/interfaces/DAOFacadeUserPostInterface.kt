package com.example.dao.interfaces

import com.example.models.UserPostModel

interface DAOFacadeUserPostInterface {
    suspend fun createUserPost(
        title: String,
        body: String?,
        postImage: String,
        owner: Int
    ): UserPostModel?

    suspend fun deleteUserPost(id: Int): Boolean
    suspend fun getUserPostById(id: Int): UserPostModel?
    suspend fun changeUserPost(
        id: Int,
        title: String,
        body: String?,
        postImage: String?
    ): UserPostModel

    suspend fun getPostsCollection(
        lastId: Int,
        range: Int
    ): List<UserPostModel>
}