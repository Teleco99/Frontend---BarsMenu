package org.example.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val email: String = "",
    val password: String = ""
)