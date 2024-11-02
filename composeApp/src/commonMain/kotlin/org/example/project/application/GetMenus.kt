package org.example.project.application

import org.example.project.domain.repository.MenuRepository
import org.example.project.domain.model.MenuCollectionModel

class GetMenus(private val repository: MenuRepository) {
    suspend operator fun invoke(): MenuCollectionModel = repository.getMenusList()
}