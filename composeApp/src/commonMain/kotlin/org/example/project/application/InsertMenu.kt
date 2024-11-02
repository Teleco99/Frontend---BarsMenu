package org.example.project.application

import org.example.project.domain.repository.MenuRepository
import org.example.project.domain.model.MenuModel
import org.example.project.domain.repository.ProductRepository

class InsertMenu(
    private val menuRepository: MenuRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(menuModel: MenuModel): MenuModel {
        // Crear String con ids
        var idsProducts: String = "["
        menuModel.products.forEachIndexed { index, product ->
            // Insertar cada producto en DB
            val insertedProduct = productRepository.insertProduct(product)
            // Guardar id para asociar al menu
            idsProducts += insertedProduct.id.toString()
            // Verificar si no es el último producto para agregar la coma
            if (index != menuModel.products.lastIndex) {
                idsProducts += ","
            }
        }
        idsProducts += "]"

        return menuRepository.insertMenu(menuModel, idsProducts)
    }
}