package org.example.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthModel (
    var token: String = "",
)