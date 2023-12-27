package be.hogent.jochensnextdinner.network

import Like
import be.hogent.jochensnextdinner.model.Like
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
data class ApiLike(
    val id: String? = null,
    val name: String,
    val category: String,
    val authorId: String,

    )

fun Flow<List<ApiLike>>.asDomainObjects(): Flow<List<Like>> {
    return map { apiLikes ->
        apiLikes.map { it.asDomainObject() }
    }
}

fun ApiLike.asDomainObject(): Like {
    return Like(
        serverId = this.id,
        name = this.name,
        category = this.category,
    )
}