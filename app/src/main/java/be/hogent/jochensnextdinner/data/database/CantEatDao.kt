package be.hogent.jochensnextdinner.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CantEatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbCantEat)

    @Update
    suspend fun update(item: dbCantEat)

    @Delete
    suspend fun delete(item: dbCantEat)
//    @Query("DELETE FROM cantEats")
//    suspend fun deleteAll()

    @Query("SELECT * from cantEats WHERE name = :name")
    fun getItem(name: String): Flow<dbCantEat>

    @Query("SELECT * from cantEats ORDER BY name ASC")
    fun getAllItems(): Flow<List<dbCantEat>>
}