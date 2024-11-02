package org.example.project.domain.service

import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.config
import org.example.project.shared.NetworkUtils
import org.example.project.domain.model.AuthModel
import org.example.project.domain.model.ProductModel

class ProductService (private val authModel: AuthModel) {

    companion object {
        private const val BASE_URL = config.API_URL
        private const val PRODUCT_URL = "$BASE_URL/api/products"
    }

    suspend fun updateProduct(productModel: ProductModel): ProductModel {
        return try {
            println("Lanzamos peticion updateProduct")
            val response: HttpResponse = NetworkUtils.httpClient.put("$PRODUCT_URL/${productModel.id}") {
                headers {
                    bearerAuth(authModel.token)
                }
                contentType(ContentType.Application.Json)
                setBody(productModel)
            }
            response.body<ProductModel>()
        } catch (e: Exception) {
            println("Error updateProduct: $e")
            // Devuelve modelo vacio
            ProductModel()
        }
    }

    suspend fun insertProduct(productModel: ProductModel): ProductModel {
        return try {
            println("Lanzamos peticion insertProduct")
            val response: HttpResponse = NetworkUtils.httpClient.post(PRODUCT_URL) {
                headers {
                    bearerAuth(authModel.token)
                }
                contentType(ContentType.Application.Json)
                setBody(productModel)
            }
            response.body<ProductModel>()
        } catch (e: Exception) {
            println("Error insertProduct: $e")
            // Devuelve modelo vacio
            ProductModel()
        }
    }

    suspend fun deleteProduct(id: Int): Boolean {
        return try {
            println("Lanzamos peticion deleteProduct")
            val response: HttpResponse = NetworkUtils.httpClient.delete("$PRODUCT_URL/$id") {
                headers {
                    bearerAuth(authModel.token)
                }
            }
            // Indica que se ha realizado la petición
            true
        } catch (e: Exception) {
            println("Error insertProduct: $e")
            // Devuelve indicación de error
            false
        }
    }
}