package org.example.project.infraestructure.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.memory.MemoryCache
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_image_burguer
import kotlinx.coroutines.launch
import org.example.project.infraestructure.UIState
import org.example.project.infraestructure.viewModel.MenuViewModel
import org.jetbrains.compose.resources.painterResource
import org.example.project.shared.PermissionCallback
import org.example.project.shared.PermissionStatus
import org.example.project.shared.PermissionType
import org.example.project.shared.SharedImage
import org.example.project.shared.createPermissionsManager
import org.example.project.shared.rememberCameraManager
import org.example.project.shared.rememberGalleryManager

class ImagePickerView(
    private val idProduct: Int,
    private val nameProduct: String,
    private val menuViewModel: MenuViewModel
) : Screen {

    @Composable
    override fun Content() {
        MaterialTheme {
            val insertedImageState by menuViewModel.insertedImageState

            val coroutineScope = rememberCoroutineScope()
            var image by remember { mutableStateOf<SharedImage?>(null) }
            var imageSourceOptionDialog by remember { mutableStateOf(value = false) }
            var launchCamera by remember { mutableStateOf(value = false) }
            var launchGallery by remember { mutableStateOf(value = false) }
            var launchSetting by remember { mutableStateOf(value = false) }
            var permissionRationalDialog by remember { mutableStateOf(value = false) }

            val permissionsManager = createPermissionsManager(object : PermissionCallback {
                override fun onPermissionStatus(
                    permissionType: PermissionType,
                    status: PermissionStatus
                ) {
                    when (status) {
                        PermissionStatus.GRANTED -> {
                            when (permissionType) {
                                PermissionType.CAMERA -> launchCamera = true
                                PermissionType.GALLERY -> launchGallery = true
                            }
                        }

                        else -> {
                            permissionRationalDialog = true
                        }
                    }
                }


            })

            val cameraManager = rememberCameraManager { sharedImage ->
                coroutineScope.launch {
                    image = sharedImage
                }
            }

            val galleryManager = rememberGalleryManager { sharedImage ->
                coroutineScope.launch {
                    image = sharedImage
                }
            }

            if (imageSourceOptionDialog) {
                imageSourceOptionDialog(onDismissRequest = {
                    imageSourceOptionDialog = false
                }, onGalleryRequest = {
                    imageSourceOptionDialog = false
                    launchGallery = true
                }, onCameraRequest = {
                    imageSourceOptionDialog = false
                    launchCamera = true
                })
            }

            if (launchGallery) {
                if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
                    galleryManager.launch()
                } else {
                    permissionsManager.askPermission(PermissionType.GALLERY)
                }
                launchGallery = false
            }

            if (launchCamera) {
                if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
                    cameraManager.launch()
                } else {
                    permissionsManager.askPermission(PermissionType.CAMERA)
                }
                launchCamera = false
            }

            if (launchSetting) {
                permissionsManager.launchSettings()
                launchSetting = false
            }

            if (permissionRationalDialog) {
                alertMessageDialog(title = "Permission Required",
                    message = "To set your profile picture, please grant this permission. You can manage permissions in your device settings.",
                    positiveButtonText = "Settings",
                    negativeButtonText = "Cancel",
                    onPositiveClick = {
                        permissionRationalDialog = false
                        launchSetting = true

                    },
                    onNegativeClick = {
                        permissionRationalDialog = false
                    })

            }

            Text(text = nameProduct)

            Box(
                modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Column {

                    when (insertedImageState) {
                        UIState.Disabled -> {}
                        is UIState.Error -> Text((insertedImageState as UIState.Error).msg)
                        UIState.Loading -> CircularProgressIndicator()
                        is UIState.Success -> {
                            Text(text="Imagen de $nameProduct actualizada con exito",
                                modifier = Modifier.background(color = Color.White))

                            // Borrar el cache para actualizar imagen de producto
                            val keyString = "https://kapploo.com/public/api/image/$idProduct"

                            SingletonImageLoader.get(LocalPlatformContext.current).
                            diskCache?.remove(keyString)

                            val key = MemoryCache.Key(keyString)

                            SingletonImageLoader.get(LocalPlatformContext.current).
                            memoryCache?.remove(key)
                        }
                    }

                    if (image != null) {
                        image!!.toImageBitmap()?.let {
                            Image(
                                bitmap = it,
                                contentDescription = nameProduct,
                                modifier = Modifier.size(100.dp).clip(CircleShape).clickable {
                                    imageSourceOptionDialog = true
                                },
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Botón para subir imagen al servidor
                        Button(onClick = {
                            menuViewModel.insertImageInProduct(image!!, idProduct)
                            //navigator.pop()
                        }) {
                            Text("Upload image")
                        }
                    } else {
                        Image(
                            modifier = Modifier.size(100.dp).clip(CircleShape).clickable {
                                imageSourceOptionDialog = true
                            },
                            painter = painterResource(Res.drawable.ic_image_burguer),
                            contentDescription = "Profile",
                        )
                    }
                }
            }
        }
    }
}