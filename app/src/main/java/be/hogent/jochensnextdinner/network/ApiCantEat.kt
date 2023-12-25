package be.hogent.jochensnextdinner.network

import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
data class ApiCantEat(
    val id: String? = null,
    val name: String,
    val authorId: String,
)

fun Flow<List<ApiCantEat>>.asDomainObjects(): Flow<List<CantEat>> {
    return map { apiCantEats ->
        apiCantEats.map { it.asDomainObject() }
    }
}

fun ApiCantEat.asDomainObject(): CantEat {
    return CantEat(
        serverId = this.id,
        name = this.name,
    )
}