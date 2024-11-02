package org.example.project.domain.service

import io.ktor.client.request.*
import org.example.project.config
import org.example.project.shared.NetworkUtils.httpClient
import org.example.project.domain.model.UserModel

class UserService() {

    // Constantes para las rutas de la API
    companion object {
        private const val BASE_URL = config.API_URL
        private const val USERS_URL = "$BASE_URL/users"
    }

    suspend fun insertUser(userModel: UserModel): UserModel? {
        TODO("Not yet implemented")
    }

    suspend fun updateUser(userId: Int, user: UserModel): Boolean {
        return try {
            httpClient.put("$USERS_URL/$userId") {
                setBody(user)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteUser(userId: Int): Boolean {
        return try {
            httpClient.delete("$USERS_URL/$userId")
            true
        } catch (e: Exception) {
            false
        }
    }
}