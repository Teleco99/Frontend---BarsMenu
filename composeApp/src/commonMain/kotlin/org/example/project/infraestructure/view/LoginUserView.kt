package org.example.project.infraestructure.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.project.domain.model.AuthModel
import org.example.project.infraestructure.UIState
import org.example.project.infraestructure.viewModel.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

class LoginUserView(private val windowSizeClass: WindowSizeClass) : Screen {

    @Composable
    override fun Content(){
        var user by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val userViewModel : UserViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow

        // Esto es igual a "val uiState = userViewModel.uiState.value"
        val loginState by userViewModel.loginState

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = user,
                onValueChange = { user = it },
                label = { Text("Usuario") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Button(onClick = {
                    navigator.push(RegisterUserView())
                }) {
                    Text("Registrarse")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    userViewModel.login(user, password)
                }) {
                    Text("Iniciar sesión")
                }
            }

            when (loginState) {
                UIState.Loading -> CircularProgressIndicator()
                is UIState.Error -> Text("Error al iniciar sesión")
                is UIState.Success<AuthModel> -> {
                    // Reseteamos login a su estado original para usarlo en caso de logout
                    userViewModel.loginState.value = UIState.Disabled
                    navigator.push(EditMenuView(windowSizeClass))
                }
                UIState.Disabled -> {}
            }

            Text(loginState.toString())
        }
    }
}