package be.hogent.jochensnextdinner.data.database

import Like
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "likes", indices = [Index(value = ["serverId"], unique = true)])
data class dbLike(
    @PrimaryKey(autoGenerate = true)
    val localId: Long,
    val serverId: String? = null,
    val name: String,
    val category: String,
)

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