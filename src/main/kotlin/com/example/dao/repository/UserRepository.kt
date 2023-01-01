package com.example.dao.repository

import com.example.dao.interfaces.DAOFacadeUserInterface
import com.example.models.UserModel
import com.example.models.UserSchema
import com.example.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime

class UserRepository : DAOFacadeUserInterface {
    private fun resultRowToUserModel(row: ResultRow) = UserModel(
        id = row[UserSchema.id],
        name = row[UserSchema.name],
        email = row[UserSchema.email],
        profileImg = row[UserSchema.profileImg],
        countOfPosts = row[UserSchema.countOfPosts],
        password = row[UserSchema.password],
        createDate = row[UserSchema.createDate]
    )

    override suspend fun allUsers(): List<UserModel> = dbQuery {
        UserSchema.selectAll().map(::resultRowToUserModel)
    }

    override suspend fun getUserById(id: Int): UserModel? = dbQuery {
        UserSchema
            .select { UserSchema.id eq id }
            .map(::resultRowToUserModel)
            .singleOrNull()
    }

    override suspend fun createUser(name: String, email: String, password: String, profileImage: String?): UserModel? = dbQuery{
        val insertStatement = UserSchema.insert {
            it[UserSchema.name] = name
            it[UserSchema.email] = email
            it[UserSchema.profileImg] = profileImage
            it[UserSchema.password] = BCrypt.hashpw(password, BCrypt.gensalt())
            it[UserSchema.createDate] = LocalDateTime.now().toString()
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUserModel)
    }

    override suspend fun deleteUserById(id: Int): Boolean = dbQuery {
        UserSchema.deleteWhere { UserSchema.id eq id } > 0
    }

    override suspend fun editUser(id: Int, name: String, profileImage: String): Boolean = dbQuery {
        UserSchema.update({ UserSchema.id eq id }) {
            it[UserSchema.name] = name
            it[UserSchema.profileImg] = profileImage
        } > 0
    }

    override suspend fun changeUserPassword(id: Int, password: String): Boolean = dbQuery {
        UserSchema.update({ UserSchema.id eq id }) {
            it[UserSchema.password] = BCrypt.hashpw(password, BCrypt.gensalt())
        } > 0
    }
}