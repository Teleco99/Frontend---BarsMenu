package org.example.project.application

import org.example.project.domain.repository.MenuRepository


class DeleteMenu(private val menuRepository: MenuRepository) {
    suspend operator fun invoke(id: Int): Boolean = menuRepository.deleteMenu(id)
}