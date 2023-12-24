package be.hogent.jochensnextdinner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [dbCantEat::class], version = 5, exportSchema = false)
abstract class CantEatDb : RoomDatabase() {

    abstract fun cantEatDao(): CantEatDao

    companion object {
        @Volatile
        private var Instance: CantEatDb? = null

        fun getDatabase(context: Context): CantEatDb {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CantEatDb::class.java, "cantEat_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}