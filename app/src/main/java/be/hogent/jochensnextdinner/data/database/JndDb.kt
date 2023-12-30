package be.hogent.jochensnextdinner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Represents the database for the application.
 *
 * This class is a singleton and provides access to the DAOs used to interact with the database.
 *
 * @property Instance The singleton instance of the database.
 */
@Database(
    entities = [dbCantEat::class, dbLike::class, dbRecipe::class],
    version = 1,
    exportSchema = false
)
abstract class JndDb : RoomDatabase() {

    /**
     * Provides access to the CantEatDao.
     *
     * @return The CantEatDao.
     */
    abstract fun cantEatDao(): CantEatDao

    /**
     * Provides access to the LikeDao.
     *
     * @return The LikeDao.
     */
    abstract fun likeDao(): LikeDao

    /**
     * Provides access to the RecipeDao.
     *
     * @return The RecipeDao.
     */
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var Instance: JndDb? = null

        /**
         * Returns the singleton instance of the database, creating it if necessary.
         *
         * @param context The context to use for creating the database.
         * @return The singleton instance of the database.
         */
        fun getDatabase(context: Context): JndDb {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, JndDb::class.java, "jnd_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}