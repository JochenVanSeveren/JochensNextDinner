package be.hogent.jochensnextdinner.data.database

import Like
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a "like" in the database.
 *
 * @property localId The unique ID for this like in the local database. This is the primary key.
 * @property serverId The unique ID for this like on the server. This is indexed and must be unique.
 * @property name The name of the like.
 * @property category The category of the like.
 */
@Entity(tableName = "likes", indices = [Index(value = ["serverId"], unique = true)])
data class dbLike(
    @PrimaryKey(autoGenerate = true)
    val localId: Long,
    val serverId: String? = null,
    val name: String,
    val category: String,
)

/**
 * Converts a list of dbLike objects to a list of Like domain objects.
 *
 * @return A list of Like domain objects.
 */
fun List<dbLike>.asDomainLikes(): List<Like> {
    return this.map {
        Like(
            localId = it.localId,
            serverId = it.serverId,
            name = it.name,
            category = it.category,
        )
    }
}