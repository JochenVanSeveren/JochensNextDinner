package be.hogent.jochensnextdinner.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.model.CantEat

@Entity(tableName = "cantEats", indices = [Index(value = ["serverId"], unique = true)])
data class dbCantEat(
    @PrimaryKey(autoGenerate = true)
    val localId: Long ,
    val serverId: String? = null,
    val name: String,
)

fun List<dbCantEat>.asDomainCantEats(): List<CantEat> {
    return this.map {
        CantEat(
            localId = it.localId,
            serverId = it.serverId,
            name = it.name,
        )
    }
}