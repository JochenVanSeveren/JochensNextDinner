package be.hogent.jochensnextdinner.network

import Like
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

/**
 * Data class representing a Like item from the API.
 *
 * @property id The server ID of the Like item.
 * @property name The name of the Like item.
 * @property category The category of the Like item.
 * @property authorId The ID of the author of the Like item.
 */
@Serializable
data class ApiLike(
    val id: String? = null,
    val name: String,
    val category: String,
    val authorId: String,
)

/**
 * Extension function to convert a Flow of List of ApiLike to a Flow of List of Like.
 * It maps each ApiLike item to a Like item.
 *
 * @return A Flow of List of Like items.
 */
fun Flow<List<ApiLike>>.asDomainObjects(): Flow<List<Like>> {
    return map { apiLikes ->
        apiLikes.map { it.asDomainObject() }
    }
}

/**
 * Extension function to convert an ApiLike item to a Like item.
 *
 * @return A Like item.
 */
fun ApiLike.asDomainObject(): Like {
    return Like(
        serverId = this.id,
        name = this.name,
        category = this.category,
    )
}