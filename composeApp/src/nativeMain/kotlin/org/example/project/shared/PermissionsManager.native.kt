package org.example.project.shared

import androidx.compose.runtime.Composable

actual class PermissionsManager actual constructor(callback: PermissionCallback) :
    PermissionHandler {
    override fun askPermission(permission: PermissionType) {
        TODO("Not yet implemented")
    }

    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return true
    }

    override fun launchSettings() {
        TODO("Not yet implemented")
    }
}

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    TODO("Not yet implemented")
}