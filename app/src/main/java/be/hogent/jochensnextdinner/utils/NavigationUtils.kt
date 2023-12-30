package be.hogent.jochensnextdinner.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import be.hogent.jochensnextdinner.R

sealed class IconResource {
    data class Vector(val vector: ImageVector) : IconResource()
    data class Drawable(val resId: Int) : IconResource()
}

enum class JochensNextDinnerScreen(
    val icon: IconResource,
    val label: Int,
    val inBottomBar: Boolean
) {
    Start(IconResource.Vector(Icons.Filled.Home), R.string.home_nav_name, false),
    CantEatScreen(IconResource.Drawable(R.drawable.skull), R.string.cant_eat_screen, true),
    LikeScreen(IconResource.Drawable(R.drawable.thumb_up), R.string.like_screen, true),
    RecipeScreen(IconResource.Drawable(R.drawable.skillet), R.string.recipe_screen, true),
}

enum class JndNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}
