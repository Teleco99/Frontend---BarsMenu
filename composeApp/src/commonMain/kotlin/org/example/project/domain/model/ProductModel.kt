package org.example.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductModel(
    val id: Int = -1,
    var name: String = "",
    var description: String = "",
    var price: String = "",
    var allergens: List<String> = emptyList()
)