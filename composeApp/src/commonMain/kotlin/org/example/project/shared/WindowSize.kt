package org.example.project.shared

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

@Composable
expect fun calculateWindowSize(): WindowSizeClass
