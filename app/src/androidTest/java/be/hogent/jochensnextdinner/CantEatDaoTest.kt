package be.hogent.jochensnextdinner

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.hogent.jochensnextdinner.data.database.CantEatDao
import be.hogent.jochensnextdinner.data.database.JndDb
import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CantEatDaoTest {
    private lateinit var cantEatDao: CantEatDao
    private lateinit var jndDb: JndDb

    private var cantEat1 = CantEat(name = "first")
    private var cantEat2 = CantEat(name = "second")

    // utility functions
    private suspend fun addOneCantEatToDb() {
        cantEatDao.insert(cantEat1.asDbCantEat())
    }

    private suspend fun addTwoCantEatsToDb() {
        cantEatDao.insert(cantEat1.asDbCantEat())
        cantEatDao.insert(cantEat2.asDbCantEat())
    }

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        jndDb = Room.inMemoryDatabaseBuilder(context, JndDb::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        cantEatDao = jndDb.cantEatDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        jndDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertCantEatIntoDB() = runBlocking {
        addOneCantEatToDb()
        val allItems = cantEatDao.getAllItems().first()
        assertEquals(allItems[0].name, cantEat1.name)
        assertEquals(allItems[0].serverId, cantEat1.serverId)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllCantEats_returnsAllCantEatsFromDB() = runBlocking {
        addTwoCantEatsToDb()
        val allItems = cantEatDao.getAllItems().first()
        assertEquals(allItems[0].name, cantEat1.name)
        assertEquals(allItems[0].serverId, cantEat1.serverId)
        assertEquals(allItems[1].name, cantEat2.name)
        assertEquals(allItems[1].serverId, cantEat2.serverId)
    }
}