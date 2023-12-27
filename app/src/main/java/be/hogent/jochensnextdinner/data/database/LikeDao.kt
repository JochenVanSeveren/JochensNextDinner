package be.hogent.jochensnextdinner.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbLike)

    @Update
    suspend fun update(item: dbLike)

    @Delete
    suspend fun delete(item: dbLike)

    @Query("SELECT * from likes WHERE name = :name")
    fun getItem(name: String): Flow<dbLike>

    @Query("SELECT * from likes ORDER BY name ASC")
    fun getAllItems(): Flow<List<dbLike>>
}