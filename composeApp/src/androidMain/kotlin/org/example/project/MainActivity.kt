package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Realiza la inyección de dependencias de di.kt
        initKoin()

        setContent {
            app()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    app()
}