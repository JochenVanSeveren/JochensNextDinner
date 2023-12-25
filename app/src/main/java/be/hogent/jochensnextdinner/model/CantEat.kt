package be.hogent.jochensnextdinner.model

import android.content.ContentValues.TAG
import android.util.Log
import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.data.database.dbCantEat
import be.hogent.jochensnextdinner.network.ApiCantEat

data class CantEat(
    val localId: Long = 0,
    val serverId: String? = null,
    val name: String,
)

fun CantEat.asDbCantEat(): dbCantEat {
    return dbCantEat(
        localId = this.localId,
        serverId = this.serverId,
        name = this.name,
    )
}

fun CantEat.asApiObject(): ApiCantEat {
    Log.d(TAG, "asApiObject: $this")
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
