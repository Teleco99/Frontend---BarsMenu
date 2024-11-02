package org.example.project.domain.service

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.config
import org.example.project.shared.NetworkUtils
import org.example.project.domain.model.AuthModel
import org.example.project.domain.model.UserModel

class AuthService(private val authModel: AuthModel) {

    companion object {
        private const val BASE_URL = config.API_URL
        private const val LOGIN_URL = "$BASE_URL/api/login"
        private const val LOGOUT_URL = "$BASE_URL/api/logout"
    }

    suspend fun login(username: String, password: String): AuthModel {
        return try {
            println("Lanzamos peticion login")
            val response: HttpResponse = NetworkUtils.httpClient.post(LOGIN_URL) {
                contentType(ContentType.Application.Json)
                setBody(UserModel(username, password))
            }
            println("Respuesta login: $response")
            response.body<AuthModel>()
        } catch (e: Exception) {
            println("Error login: $e")
            // Devuelve modelo vacio
            AuthModel()
        }
    }

    suspend fun logout(): AuthModel {
        return try {
            println("Lanzamos peticion logout")
            val response: HttpResponse = NetworkUtils.httpClient.post(LOGOUT_URL) {
                headers {
                    bearerAuth(authModel.token)
                }
            }
            println("Respuesta logout: $response")
            response.body<AuthModel>()
        } catch (e: Exception) {
            println("Error logout: $e")
            // Devuelve modelo vacio
            AuthModel()
        }
    }
}