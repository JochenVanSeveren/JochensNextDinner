package be.hogent.jochensnextdinner

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.hogent.jochensnextdinner.data.database.JndDb
import be.hogent.jochensnextdinner.data.database.RecipeDao
import be.hogent.jochensnextdinner.model.Recipe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {
    private lateinit var recipeDao: RecipeDao
    private lateinit var jndDb: JndDb

    private var recipe1 = Recipe(slug = "first", title = "title1", ingredients = listOf("ingredient1"), optionalIngredients = listOf("optionalIngredient1"), herbs = listOf("herb1"), steps = listOf("step1"), image = "image1")
    private var recipe2 = Recipe(slug = "second", title = "title2", ingredients = listOf("ingredient2"), optionalIngredients = listOf("optionalIngredient2"), herbs = listOf("herb2"), steps = listOf("step2"), image = "image2")

    // utility functions
    private suspend fun addOneRecipeToDb() {
        recipeDao.insert(recipe1.asDbRecipe())
    }

    private suspend fun addTwoRecipesToDb() {
        recipeDao.insert(recipe1.asDbRecipe())
        recipeDao.insert(recipe2.asDbRecipe())
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
        recipeDao = jndDb.recipeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        jndDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertRecipeIntoDB() = runBlocking {
        addOneRecipeToDb()
        val allItems = recipeDao.getAllItems().first()
        assertEquals(allItems[0].slug, recipe1.slug)
        assertEquals(allItems[0].serverId, recipe1.serverId)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllRecipes_returnsAllRecipesFromDB() = runBlocking {
        addTwoRecipesToDb()
        val allItems = recipeDao.getAllItems().first()
        assertEquals(allItems[0].slug, recipe1.slug)
        assertEquals(allItems[0].serverId, recipe1.serverId)
        assertEquals(allItems[1].slug, recipe2.slug)
        assertEquals(allItems[1].serverId, recipe2.serverId)
    }
}