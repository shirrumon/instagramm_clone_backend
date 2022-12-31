package com.example.models

import org.jetbrains.exposed.sql.Table

data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val profileImg: String?,
    val countOfPosts: Int = 0,
    val password: String,
    val createDate: String
)

object UserSchema : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 64)
    val email = varchar("email", 64).uniqueIndex()
    val profileImg = varchar("profileImg", 1024).nullable()
    val countOfPosts = integer("countOfPosts")
    val password = varchar("password", 128)
    val createDate = varchar("createDate", 32)

    override val primaryKey = PrimaryKey(id)
}