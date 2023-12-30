package be.hogent.jochensnextdinner.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the "recipes" table in the database.
 *
 * This interface provides methods to perform operations on the "recipes" table.
 */
@Dao
interface RecipeDao {

    /**
     * Inserts a new recipe into the "recipes" table.
     * If a recipe with the same primary key already exists, it will be replaced.
     *
     * @param item The recipe to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbRecipe)

    /**
     * Updates an existing recipe in the "recipes" table.
     *
     * @param item The recipe to update.
     */
    @Update
    suspend fun update(item: dbRecipe)

    /**
     * Deletes a recipe from the "recipes" table.
     *
     * @param item The recipe to delete.
     */
    @Delete
    suspend fun delete(item: dbRecipe)

    /**
     * Retrieves a recipe from the "recipes" table by its local ID.
     *
     * @param id The local ID of the recipe to retrieve.
     * @return A Flow emitting the retrieved recipe.
     */
    @Query("SELECT * from recipes WHERE localId = :id")
    fun getItem(id: Long): Flow<dbRecipe>

    /**
     * Retrieves all recipes from the "recipes" table, ordered by title in ascending order.
     *
     * @return A Flow emitting a list of all recipes.
     */
    @Query("SELECT * from recipes ORDER BY title ASC")
    fun getAllItems(): Flow<List<dbRecipe>>
}