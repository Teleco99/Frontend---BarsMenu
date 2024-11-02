package org.example.project.shared

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkUtils {
    val httpClient = HttpClient() {
        install(ContentNegotiation){
            json(json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true // Coercionar nulos a valores predeterminados
            }, contentType = ContentType.Any)
        }

        install(Logging) {
            logger = object: Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HTTP Client", message = message)
                }
            }
            level = LogLevel.ALL
        }.also { initNapier() }
    }
}

expect fun initNapier()