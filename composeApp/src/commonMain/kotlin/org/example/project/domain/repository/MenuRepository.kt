package org.example.project.domain.repository

import org.example.project.domain.model.MenuCollectionModel
import org.example.project.domain.model.MenuModel
import org.example.project.domain.service.MenuService

class MenuRepository(private val menuService: MenuService) {
    suspend fun getMenusList(): MenuCollectionModel {
        return menuService.fetchMenusList()
    }

    suspend fun updateMenu(menuModel: MenuModel): MenuModel {
        return menuService.updateMenu(menuModel)
    }

    suspend fun insertMenu(menuModel: MenuModel, idsProducts: String): MenuModel {
        return menuService.insertMenu(menuModel, idsProducts)
    }

    suspend fun deleteMenu(id: Int): Boolean {
        return menuService.deleteMenu(id)
    }
}