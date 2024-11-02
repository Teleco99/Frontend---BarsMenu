package org.example.project.domain.service

import coil3.PlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.example.project.config
import org.example.project.domain.model.AuthModel
import org.example.project.shared.NetworkUtils
import org.example.project.shared.SharedImage

class ImageService(private val authModel: AuthModel) {

    companion object {
        private const val BASE_URL = config.API_URL
        private const val IMAGE_URL = "$BASE_URL/api/image"
    }

    suspend fun insertImageInProduct(image: SharedImage, idProduct: Int): Int {
        return try {
            println("Lanzamos peticion insertImageInProduct")
            NetworkUtils.httpClient.post(IMAGE_URL) {
                val imageByteArray = image.toByteArray()
                    ?: throw Exception("Exception: toByteArray returned null")

                setBody(
                    MultiPartFormDataContent(
                    formData {
                        append("idProduct", idProduct.toString())
                        append("image", imageByteArray, Headers.build {
                            bearerAuth(authModel.token)
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"image.png\"")
                        })
                    },
                    boundary = "image"
                )
                )
                onUpload { bytesSentTotal, contentLength ->
                    println("Sent $bytesSentTotal bytes from $contentLength")
                }
            }
            // Indica que se ha realizado la petición devolviendo el idProduct
            idProduct
        } catch (e: Exception) {
            println("Error insertProduct: $e")
            // Devuelve indicación de error con -1
            -1
        }
    }

    fun getImageRequest(idProduct: Int, context: PlatformContext): ImageRequest {
        val headers = NetworkHeaders.Builder()
            .add("Authorization", "Bearer ${authModel.token}")
            .build()

        val imageUrl = "$IMAGE_URL/$idProduct"

        return ImageRequest.Builder(context)
            .data(imageUrl)
            .httpHeaders(headers)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}