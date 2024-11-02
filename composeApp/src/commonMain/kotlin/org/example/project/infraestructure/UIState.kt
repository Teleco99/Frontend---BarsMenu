package org.example.project.infraestructure

sealed class UIState<out T> {
    data object Disabled : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
    data class Success<out T>(val model: T) : UIState<T>()
    data class Error(val msg: String) : UIState<Nothing>()
}