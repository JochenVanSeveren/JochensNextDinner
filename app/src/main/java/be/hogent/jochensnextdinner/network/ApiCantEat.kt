package be.hogent.jochensnextdinner.network

import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

/**
 * Data class representing a CantEat item from the API.
 *
 * @property id The server ID of the CantEat item.
 * @property name The name of the CantEat item.
 * @property authorId The ID of the author of the CantEat item.
 */
@Serializable
data class ApiCantEat(
    val id: String? = null,
    val name: String,
    val authorId: String,
)

/**
 * Extension function to convert a Flow of List of ApiCantEat to a Flow of List of CantEat.
 * It maps each ApiCantEat item to a CantEat item.
 *
 * @return A Flow of List of CantEat items.
 */
fun Flow<List<ApiCantEat>>.asDomainObjects(): Flow<List<CantEat>> {
    return map { apiCantEats ->
        apiCantEats.map { it.asDomainObject() }
    }
}

/**
 * Extension function to convert an ApiCantEat item to a CantEat item.
 *
 * @return A CantEat item.
 */
fun ApiCantEat.asDomainObject(): CantEat {
    return CantEat(
        serverId = this.id,
        name = this.name,
    )
}