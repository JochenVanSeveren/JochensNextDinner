package be.hogent.jochensnextdinner.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import be.hogent.jochensnextdinner.R

enum class JochensNextDinnerScreen(
    val iconResource: Int?,
    val vectorIcon: ImageVector?,
    val label: Int,
    val inBottomBar: Boolean
) {
    Start(null, Icons.Filled.Home, R.string.home_nav_name, false),
    CantEatScreen(R.drawable.skull, null, R.string.cant_eat_screen, true),
    LikeScreen(R.drawable.thumb_up, null, R.string.like_screen, true),
    RecipeScreen(R.drawable.skillet, null, R.string.recipe_screen, true),
}

enum class JndNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}
