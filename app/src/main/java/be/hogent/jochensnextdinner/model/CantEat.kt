package be.hogent.jochensnextdinner.model

import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.data.database.dbCantEat
import be.hogent.jochensnextdinner.network.ApiCantEat

/**
 * This class represents a CantEat entity.
 *
 * @property localId The local ID of the CantEat.
 * @property serverId The server ID of the CantEat.
 * @property name The name of the CantEat.
 */
data class CantEat(
    val localId: Long = 0,
    val serverId: String? = null,
    val name: String,
) {
    /**
     * Converts this CantEat to a dbCantEat.
     *
     * @return The dbCantEat representation of this CantEat.
     */
    fun asDbCantEat(): dbCantEat {
        return dbCantEat(
            localId = this.localId,
            serverId = this.serverId,
            name = this.name,
        )
    }

    /**
     * Converts this CantEat to an ApiCantEat.
     *
     * @return The ApiCantEat representation of this CantEat.
     */
    fun asApiObject(): ApiCantEat {
        return if (this.serverId != null) {
            ApiCantEat(
                id = this.serverId,
                name = this.name,
                authorId = BuildConfig.AUTHOR_ID,
            )
        } else {
            ApiCantEat(
                name = this.name,
                authorId = BuildConfig.AUTHOR_ID,
            )
        }
    }
}