package com.example.dao.repository

import com.example.dao.DatabaseFactory.dbQuery
import com.example.dao.interfaces.DAOFacadeUserPostInterface
import com.example.models.UserPostModel
import com.example.models.UserPostSchema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.time.LocalDateTime

class UserPostRepository : DAOFacadeUserPostInterface {
    private fun resultRowToUserPostModel(row: ResultRow) = UserPostModel(
        id = row[UserPostSchema.id],
        title = row[UserPostSchema.title],
        body = row[UserPostSchema.body],
        postImage = row[UserPostSchema.postImage],
        likes = row[UserPostSchema.likes],
        owner = row[UserPostSchema.owner],
        createDate = row[UserPostSchema.createDate],
    )
    override suspend fun createUserPost(title: String, body: String?, postImage: String, owner: Int): UserPostModel? = dbQuery {
        val insertStatement = UserPostSchema.insert {
            it[UserPostSchema.title] = title
            it[UserPostSchema.body] = body
            it[UserPostSchema.postImage] = postImage
            it[UserPostSchema.owner] = owner
            it[UserPostSchema.createDate] = LocalDateTime.now().toString()
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUserPostModel)
    }

    override suspend fun deleteUserPost(id: Int): Boolean = dbQuery {
        UserPostSchema.deleteWhere { UserPostSchema.id eq id } > 0
    }

    override suspend fun getUserPostById(id: Int): UserPostModel? = dbQuery {
        UserPostSchema
            .select { UserPostSchema.id eq id }
            .map(::resultRowToUserPostModel)
            .singleOrNull()
    }

    override suspend fun changeUserPost(id: Int, title: String, body: String?, postImage: String?): UserPostModel {
        TODO("Not yet implemented")
    }

    override suspend fun getPostsCollection(lastId: Int, range: Int): List<UserPostModel> = dbQuery {
        val result: MutableList<UserPostModel> = mutableListOf()
        TransactionManager.current().exec("select * from UserPostSchema where id between $lastId and ${lastId+range}") { qResult ->
            while (qResult.next()) {
                result.add(
                    UserPostModel(
                        qResult.getInt("id"),
                        qResult.getString("title"),
                        qResult.getString("body"),
                        qResult.getString("postImage"),
                        qResult.getInt("likes"),
                        qResult.getInt("owner"),
                        qResult.getString("createDate")
                    )
                )
            }
        }
        result
    }
}