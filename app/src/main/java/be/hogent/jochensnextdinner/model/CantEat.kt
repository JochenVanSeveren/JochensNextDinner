package be.hogent.jochensnextdinner.model

import be.hogent.jochensnextdinner.BuildConfig
import kotlinx.serialization.Serializable

@Serializable
data class CantEat(
    val id: String,
    val name: String,
    val authorId: String,
    val createdAt: String,
    val updatedAt: String
) {
    constructor(name: String) : this(
        id = "",
        name = name,
        authorId = BuildConfig.AUTHOR_ID,
        createdAt = "",
        updatedAt = ""
    )
}