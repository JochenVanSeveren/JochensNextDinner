package be.hogent.jochensnextdinner.model

data class Recipe(
    val id: String,
    val slug: String,
    val title: String,
    val ingredients: List<String>,
    val optionalIngredients: List<String>,
    val herbs: List<String>,
    val steps: List<String>,
    val image: String?,
    val authorId: String,
    val createdAt: String,  // Assuming ISO 8601 format
    val updatedAt: String
)
