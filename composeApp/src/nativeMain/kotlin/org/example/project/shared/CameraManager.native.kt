package org.example.project.shared

@Composable
actual fun rememberCameraManager(onResult: (SharedImage?) -> Unit): CameraManager {
    TODO("Not yet implemented")
}

actual class CameraManager actual constructor(onLaunch: () -> Unit) {
    actual fun launch() {
    }
}