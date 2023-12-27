package be.hogent.jochensnextdinner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(
    entities = [dbCantEat::class, dbLike::class, dbRecipe::class],
    version = 1,
    exportSchema = false
)
abstract class JndDb : RoomDatabase() {

    abstract fun cantEatDao(): CantEatDao
    abstract fun likeDao(): LikeDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var Instance: JndDb? = null

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