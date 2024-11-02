package org.example.project.application

import coil3.PlatformContext
import coil3.request.ImageRequest
import org.example.project.domain.service.ImageService

class GetImageRequest(private val imageService: ImageService) {
    operator fun invoke(idProduct: Int, context: PlatformContext):
            ImageRequest = imageService.getImageRequest(idProduct, context)
}