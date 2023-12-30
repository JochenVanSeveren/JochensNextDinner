package be.hogent.jochensnextdinner.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the "likes" table in the database.
 *
 * This interface provides methods to perform operations on the "likes" table.
 */
@Dao
interface LikeDao {

    /**
     * Inserts a new like into the "likes" table.
     * If a like with the same primary key already exists, it will be replaced.
     *
     * @param item The like to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbLike)

    /**
     * Updates an existing like in the "likes" table.
     *
     * @param item The like to update.
     */
    @Update
    suspend fun update(item: dbLike)

    /**
     * Deletes a like from the "likes" table.
     *
     * @param item The like to delete.
     */
    @Delete
    suspend fun delete(item: dbLike)

    /**
     * Retrieves a like from the "likes" table by its name.
     *
     * @param name The name of the like to retrieve.
     * @return A Flow emitting the retrieved like.
     */
    @Query("SELECT * from likes WHERE name = :name")
    fun getItem(name: String): Flow<dbLike>

    /**
     * Retrieves all likes from the "likes" table, ordered by name in ascending order.
     *
     * @return A Flow emitting a list of all likes.
     */
    @Query("SELECT * from likes ORDER BY name ASC")
    fun getAllItems(): Flow<List<dbLike>>
}