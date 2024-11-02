package org.example.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuCollectionModel(
    val data: List<MenuModel> = emptyList()
)