package org.example.project.application

import org.example.project.domain.repository.ProductRepository

class DeleteProduct(private val productRepository: ProductRepository) {
    suspend operator fun invoke(id: Int): Boolean = productRepository.deleteProduct(id)
}