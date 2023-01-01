package com.example.models

import com.example.models.UserSchema.autoIncrement
import org.jetbrains.exposed.sql.Table

data class UserPostModel(
    val id: Int,
    val title: String,
    val body: String?,
    val postImage: String?,
    val likes: Int = 0,
    val owner: Int,
    val createDate: String
)

object UserPostSchema : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 64)
    val body = text("body").nullable()
    val postImage = varchar("postImage", 1024).nullable()
    val likes = integer("likes")
    val owner = integer("owner")
    val createDate = varchar("createDate", 32)

    override val primaryKey = PrimaryKey(UserSchema.id)
}