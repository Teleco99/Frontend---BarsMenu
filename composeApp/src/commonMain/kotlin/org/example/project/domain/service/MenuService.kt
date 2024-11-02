package org.example.project.domain.service

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.config
import org.example.project.shared.NetworkUtils
import org.example.project.domain.model.MenuCollectionModel
import org.example.project.domain.model.AuthModel
import org.example.project.domain.model.MenuModel

class MenuService(private val authModel: AuthModel) {

    companion object {
        private const val BASE_URL = config.API_URL
        private const val MENU_URL = "$BASE_URL/api/menus"
    }

    suspend fun fetchMenusList(): MenuCollectionModel {
        return try {
            println("Lanzamos peticion fetchMenusList")
            val response: HttpResponse = NetworkUtils.httpClient.get(MENU_URL) {
                headers {
                    bearerAuth(authModel.token)
                }
            }
            response.body<MenuCollectionModel>()
        } catch (e: Exception) {
            println("Error fetchMenusList: $e")
            // Devuelve modelo vacio
            MenuCollectionModel()
        }
    }

    suspend fun updateMenu(menuModel: MenuModel): MenuModel {
        return try {
            println("Lanzamos peticion updateMenu")
            val response: HttpResponse = NetworkUtils.httpClient.put("$MENU_URL/${menuModel.id}") {
                headers {
                    bearerAuth(authModel.token)
                }
                contentType(ContentType.Application.Json)

                val name = menuModel.name
                setBody("{ \"name\": \"$name\" }")
            }
            response.body<MenuModel>()
        } catch (e: Exception) {
            println("Error update: $e")
            // Devuelve modelo vacio
            MenuModel()
        }
    }

    suspend fun insertMenu(menuModel: MenuModel, idsProducts: String): MenuModel {
        return try {
            println("Lanzamos peticion insertMenu")
            val response: HttpResponse = NetworkUtils.httpClient.post(MENU_URL) {
                headers {
                    bearerAuth(authModel.token)
                }
                contentType(ContentType.Application.Json)

                val name = menuModel.name
                setBody("""
                    {
                    "name": "$name",
                    "products": $idsProducts
                    }
                    """.trimIndent()) // Utiliza trimIndent para eliminar la indentación adicional
            }
            response.body<MenuModel>()
        } catch (e: Exception) {
            println("Error insertMenu: $e")
            // Devuelve modelo vacio
            MenuModel()
        }
    }

    suspend fun deleteMenu(id: Int): Boolean {
        return try {
            println("Lanzamos peticion deleteMenu")
            val response: HttpResponse = NetworkUtils.httpClient.delete("$MENU_URL/$id") {
                headers {
                    bearerAuth(authModel.token)
                }
            }
            // Indica que se ha realizado la petición
            true
        } catch (e: Exception) {
            println("Error insertMenu: $e")
            // Devuelve indicación de error
            false
        }
    }
}