package be.hogent.jochensnextdinner.ui

import androidx.lifecycle.ViewModel
import be.hogent.jochensnextdinner.data.fake.cantEatList
import be.hogent.jochensnextdinner.data.fake.likeList
import be.hogent.jochensnextdinner.data.fake.recipeList
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.model.Like
import be.hogent.jochensnextdinner.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JochensNextDinnerViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(recipeList)
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _cantEats = MutableStateFlow<List<CantEat>>(cantEatList)
    val cantEats: StateFlow<List<CantEat>> = _cantEats

    private val _likes = MutableStateFlow<List<Like>>(likeList)
    val likes: StateFlow<List<Like>> = _likes

}
