package be.hogent.jochensnextdinner.fake

import be.hogent.jochensnextdinner.network.ApiCantEat

object FakeCantEatDataSource {
    const val nameOne = "cant eat one"
    const val nameTwo = "cant eat two"

    val cantEats = listOf(
        ApiCantEat(name = nameOne, authorId = "author1"),
        ApiCantEat(name = nameTwo, authorId = "author2"),
    )
}