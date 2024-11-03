package org.example.project

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    // Realiza la inyección de dependencias de di.kt
    initKoin()

    ComposeViewport(document.body!!) {
        app()
    }
}