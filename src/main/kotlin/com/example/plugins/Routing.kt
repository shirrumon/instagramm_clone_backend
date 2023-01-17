package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.repository.UserRepository
import com.example.models.UserModel
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Application.configureRouting() {
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    routing {
        get("/") {
            call.respondText("sdfsdfs My Lord!")
        }

        post("/login") {
            val user = call.receive<UserModel>()
            val userInstance = UserRepository().getUserByEmail(user.email)

            if(userInstance !== null) {
                if(BCrypt.checkpw(userInstance.password, user.password)) {
                    val token = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("email", user.email)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                        .sign(Algorithm.HMAC256(secret))
                    call.respond(hashMapOf("token" to token))
                } else {
                    call.respondText("Wrong password")
                }
            } else {
                call.respondText("User doesnt exist")
            }
        }

        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("email").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}
