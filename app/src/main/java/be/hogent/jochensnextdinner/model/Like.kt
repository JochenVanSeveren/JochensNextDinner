package be.hogent.jochensnextdinner.model

import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.data.database.dbLike
import be.hogent.jochensnextdinner.network.ApiLike

data class Like(
    val localId: Long = 0,
    val serverId: String? = null,
    val name: String,
    val category: String,
) {
    fun asDbLike(): dbLike {
        return dbLike(
            localId = this.localId,
            serverId = this.serverId,
            name = this.name,
            category = this.category
        )
    }

    fun asApiObject(): ApiLike {
        return if (this.serverId != null) {
            ApiLike(
                id = this.serverId,
                name = this.name,
                category = this.category,
                authorId = BuildConfig.AUTHOR_ID,
            )
        } else {
            ApiLike(
                name = this.name,
                category = this.category,
                authorId = BuildConfig.AUTHOR_ID,
            )
        }
    }
}