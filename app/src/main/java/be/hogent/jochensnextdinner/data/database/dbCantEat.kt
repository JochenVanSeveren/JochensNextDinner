package be.hogent.jochensnextdinner.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.hogent.jochensnextdinner.model.CantEat

@Entity(tableName = "cantEats")
data class dbCantEat(
    @PrimaryKey
    val name: String,
    val authorId: String,
    val createdAt: String,
    val updatedAt: String
)

fun dbCantEat.asDomainCantEat(): CantEat {
    return CantEat(
        this.name,
        this.authorId,
        this.createdAt,
        this.updatedAt
    )
}

fun CantEat.asDbCantEat(): dbCantEat {
    return dbCantEat(
        name = this.name,
        authorId = this.authorId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<dbCantEat>.asDomainCantEats(): List<CantEat> {
    return this.map {
        CantEat(it.name, it.authorId, it.createdAt, it.updatedAt)
    }
}