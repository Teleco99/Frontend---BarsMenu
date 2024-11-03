package org.example.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import org.example.project.domain.model.AuthModel
import org.example.project.infraestructure.view.EditMenuView
import org.example.project.infraestructure.view.LoginUserView
import org.example.project.shared.calculateWindowSize
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
@Preview
fun app() {

    // Inyecta el singleton ImageLoader
    // TODO borrar .logger en producción
    SingletonImageLoader.setSafe { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }

    // Setea instancia de Koin a contexto de Compose
    KoinContext {
        MaterialTheme {
            val authModel = koinInject<AuthModel>()

            // Si no hay token, navega a login
            if (authModel.token.isEmpty()) {
                Navigator(screen = LoginUserView()) { navigator ->
                    SlideTransition(navigator)
                }
            }
            // Si hay token navega al home del rol correspondiente
            else {
                Navigator(screen = EditMenuView()) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}




