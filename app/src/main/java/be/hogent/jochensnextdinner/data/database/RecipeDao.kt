package be.hogent.jochensnextdinner.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbRecipe)

    @Update
    suspend fun update(item: dbRecipe)

    @Delete
    suspend fun delete(item: dbRecipe)

    @Query("SELECT * from recipes WHERE slug = :slug")
    fun getItem(slug: String): Flow<dbRecipe>

    @Query("SELECT * from recipes ORDER BY title ASC")
    fun getAllItems(): Flow<List<dbRecipe>>
}