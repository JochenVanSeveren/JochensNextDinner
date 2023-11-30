package be.hogent.jochensnextdinner.model

data class Like(
    val id: String,
    val name: String,
    val category: String,
    val authorId: String,
    val createdAt: String,  // Assuming ISO 8601 format
    val updatedAt: String
)
