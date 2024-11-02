package org.example.project.application

import org.example.project.domain.repository.MenuRepository
import org.example.project.domain.model.MenuModel
import org.example.project.domain.model.ProductModel
import org.example.project.domain.repository.ProductRepository

class UpdateMenu(
    private val menuRepository: MenuRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(menuModel: MenuModel): MenuModel {
        // Crear String con ids
        var idsProducts: String = "["
        menuModel.products.forEach {product ->
            val insertedProduct : ProductModel
            if(product.id == -1){
                // Insertar producto en DB
                insertedProduct = productRepository.insertProduct(product)
            }else {
                // Actualizar producto del menu
                insertedProduct = productRepository.updateProduct(product)
            }

            // Guardar id para asociar al menu
            idsProducts += insertedProduct.id.toString() + ","
        }
        return menuRepository.updateMenu(menuModel)
    }
}