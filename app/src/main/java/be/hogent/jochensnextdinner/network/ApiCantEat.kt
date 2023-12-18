package be.hogent.jochensnextdinner.network

import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
data class ApiCantEat(
    val name: String,
    val authorId: String,
    val createdAt: String,
    val updatedAt: String
)

fun Flow<List<ApiCantEat>>.asDomainObjects(): Flow<List<CantEat>> {
    return map {
        it.asDomainObjects()
    }
}

fun List<ApiCantEat>.asDomainObjects(): List<CantEat> {
    return this.map {
        CantEat(it.name, it.authorId, it.createdAt, it.updatedAt)
    }
}