package be.hogent.jochensnextdinner.model

import be.hogent.jochensnextdinner.BuildConfig

data class CantEat(
    val name: String,
    val authorId: String = BuildConfig.AUTHOR_ID,
    val createdAt: String = "",
    val updatedAt: String = "",
)
