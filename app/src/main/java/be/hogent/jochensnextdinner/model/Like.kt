import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.data.database.dbLike
import be.hogent.jochensnextdinner.network.ApiLike

/**
 * This class represents a Like entity.
 *
 * @property localId The local ID of the Like.
 * @property serverId The server ID of the Like.
 * @property name The name of the Like.
 * @property category The category of the Like.
 */
data class Like(
    val localId: Long = 0,
    val serverId: String? = null,
    val name: String,
    val category: String,
) {
    /**
     * Converts this Like to a dbLike.
     *
     * @return The dbLike representation of this Like.
     */
    fun asDbLike(): dbLike {
        return dbLike(
            localId = this.localId,
            serverId = this.serverId,
            name = this.name,
            category = this.category
        )
    }

    /**
     * Converts this Like to an ApiLike.
     *
     * @return The ApiLike representation of this Like.
     */
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