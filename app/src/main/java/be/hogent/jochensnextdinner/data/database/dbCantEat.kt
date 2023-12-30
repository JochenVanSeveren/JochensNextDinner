package be.hogent.jochensnextdinner.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import be.hogent.jochensnextdinner.model.CantEat

/**
 * Represents a CantEat entity in the database.
 *
 * @property localId The unique ID for this CantEat in the local database. This is the primary key.
 * @property serverId The unique ID for this CantEat in the server database.
 * @property name The name of this CantEat.
 */
@Entity(tableName = "cantEats", indices = [Index(value = ["serverId"], unique = true)])
data class dbCantEat(
    @PrimaryKey(autoGenerate = true)
    val localId: Long,
    val serverId: String? = null,
    val name: String,
)

/**
 * Extension function to convert a list of dbCantEat objects to a list of CantEat domain models.
 *
 * @return A list of CantEat domain models.
 */
fun List<dbCantEat>.asDomainCantEats(): List<CantEat> {
    return this.map {
        CantEat(
            localId = it.localId,
            serverId = it.serverId,
            name = it.name,
        )
    }
}