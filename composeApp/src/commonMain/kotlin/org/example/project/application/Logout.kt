package org.example.project.application

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.example.project.config
import org.example.project.domain.model.AuthModel
import org.example.project.domain.service.AuthService

class Logout(
    private val authService: AuthService,
    private val authModel: AuthModel
) {
    suspend operator fun invoke(): AuthModel {
        // Borramos token de DB
        val newAuthModel = authService.logout()

        if (newAuthModel.token == "deleted") {
            // Borramos token de almacenamiento local (Settings) y modelo
            val settings: Settings = Settings()
            settings[config.TOKEN_KEY] = ""
            authModel.token = ""
        }

        return newAuthModel
    }
}