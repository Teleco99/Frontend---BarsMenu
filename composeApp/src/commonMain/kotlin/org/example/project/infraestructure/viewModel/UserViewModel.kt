package org.example.project.infraestructure.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.launch
import org.example.project.application.Logout
import org.example.project.config
import org.example.project.domain.model.AuthModel
import org.example.project.domain.service.AuthService
import org.example.project.infraestructure.UIState

class UserViewModel(
    private val authService: AuthService,
    private val authModel: AuthModel,
    private val logoutUseCase: Logout
) : ViewModel() {
    val loginState: MutableState<UIState<AuthModel>> = mutableStateOf(UIState.Disabled)
    val logoutState: MutableState<UIState<AuthModel>> = mutableStateOf(UIState.Disabled)

    private val settings: Settings = Settings()

    fun login(user: String, password: String) {
        // Lanzamos hilo para realizar petición
        viewModelScope.launch {
            // Indicamos lanzamiento de petición con loading
            loginState.value = UIState.Loading
            val newAuthModel = authService.login(user, password)

            // El token vacio indica un error de autentificación
            if (newAuthModel.token.isBlank()) {
                loginState.value = UIState.Error("email o contraseña incorrectos")
            } else {
                loginState.value = UIState.Success(newAuthModel)
                // Guardamos token en almacenamiento local y en modelo
                settings[config.TOKEN_KEY] = newAuthModel.token
                authModel.token = newAuthModel.token
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutState.value = UIState.Loading
            val newAuthModel = logoutUseCase()
            if(newAuthModel.token == "deleted") {
                logoutState.value = UIState.Success(newAuthModel)
            } else {
                logoutState.value = UIState.Error("Error al cerrar sesión")
            }
        }
    }
}