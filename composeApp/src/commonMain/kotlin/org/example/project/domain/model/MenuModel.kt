package org.example.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuModel(
    val id: Int = -1,
    var name: String = "",
    var products: List<ProductModel> = emptyList()
)