package org.example.project.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import org.example.project.domain.model.ProductModel
import org.example.project.domain.service.ImageService
import org.example.project.domain.service.ProductService
import org.example.project.shared.SharedImage

class ProductRepository(
    private val productService: ProductService,
    private val imageService: ImageService
    ) {
    suspend fun updateProduct(productModel: ProductModel): ProductModel {
        return productService.updateProduct(productModel)
    }

    suspend fun insertProduct(productModel: ProductModel): ProductModel {
        return productService.insertProduct(productModel)
    }

    suspend fun deleteProduct(id: Int): Boolean {
        return productService.deleteProduct(id)
    }

    suspend fun insertImageInProduct(image: SharedImage, idProduct: Int): Int {
        return imageService.insertImageInProduct(image, idProduct)
    }
}