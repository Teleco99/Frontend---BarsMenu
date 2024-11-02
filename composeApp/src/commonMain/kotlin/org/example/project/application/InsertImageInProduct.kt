package org.example.project.application

import org.example.project.domain.repository.ProductRepository
import org.example.project.shared.SharedImage

class InsertImageInProduct(private val productRepository: ProductRepository) {
    suspend operator fun invoke(image: SharedImage, idProduct: Int): Int {
        return productRepository.insertImageInProduct(image, idProduct)
    }
}