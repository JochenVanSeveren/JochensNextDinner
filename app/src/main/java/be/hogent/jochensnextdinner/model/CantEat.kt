package be.hogent.jochensnextdinner.model

import kotlinx.serialization.Serializable

@Serializable
data class CantEat(
    val id: String,
    val name: String,
    val authorId: String,
    val createdAt: String,
    val updatedAt: String
)
