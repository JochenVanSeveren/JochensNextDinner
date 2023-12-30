package be.hogent.jochensnextdinner.dao

import Like
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.hogent.jochensnextdinner.data.database.JndDb
import be.hogent.jochensnextdinner.data.database.LikeDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LikeDaoTest {
    private lateinit var likeDao: LikeDao
    private lateinit var jndDb: JndDb

    private var like1 = Like(name = "first", category = "category1")
    private var like2 = Like(name = "second", category = "category2")

    // utility functions
    private suspend fun addOneLikeToDb() {
        likeDao.insert(like1.asDbLike())
    }

    private suspend fun addTwoLikesToDb() {
        likeDao.insert(like1.asDbLike())
        likeDao.insert(like2.asDbLike())
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
        likeDao = jndDb.likeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        jndDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertLikeIntoDB() = runBlocking {
        addOneLikeToDb()
        val allItems = likeDao.getAllItems().first()
        assertEquals(allItems[0].name, like1.name)
        assertEquals(allItems[0].serverId, like1.serverId)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllLikes_returnsAllLikesFromDB() = runBlocking {
        addTwoLikesToDb()
        val allItems = likeDao.getAllItems().first()
        assertEquals(allItems[0].name, like1.name)
        assertEquals(allItems[0].serverId, like1.serverId)
        assertEquals(allItems[1].name, like2.name)
        assertEquals(allItems[1].serverId, like2.serverId)
    }
}