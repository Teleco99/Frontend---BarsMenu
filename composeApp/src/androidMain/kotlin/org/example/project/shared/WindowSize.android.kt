package org.example.project.shared

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import org.example.project.MainApplication

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun calculateWindowSize(): WindowSizeClass {
    return calculateWindowSizeClass(MainApplication.currentActivity!!)
}