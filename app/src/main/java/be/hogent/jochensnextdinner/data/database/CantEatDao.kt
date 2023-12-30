package be.hogent.jochensnextdinner.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the CantEat table.
 * This class provides methods for interacting with the CantEat table in the database.
 */
@Dao
interface CantEatDao {

    /**
     * Insert a new CantEat into the database.
     * If a CantEat with the same name already exists, it will be replaced.
     * @param item The CantEat to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbCantEat)

    /**
     * Update an existing CantEat in the database.
     * @param item The CantEat to update.
     */
    @Update
    suspend fun update(item: dbCantEat)

    /**
     * Delete a CantEat from the database.
     * @param item The CantEat to delete.
     */
    @Delete
    suspend fun delete(item: dbCantEat)

    /**
     * Get a CantEat from the database by its name.
     * @param name The name of the CantEat to retrieve.
     * @return A Flow emitting the CantEat with the given name.
     */
    @Query("SELECT * from cantEats WHERE name = :name")
    fun getItem(name: String): Flow<dbCantEat>

    /**
     * Get all CantEats from the database.
     * The CantEats are ordered by their names in ascending order.
     * @return A Flow emitting a list of all CantEats.
     */
    @Query("SELECT * from cantEats ORDER BY name ASC")
    fun getAllItems(): Flow<List<dbCantEat>>
}