package be.hogent.jochensnextdinner.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import be.hogent.jochensnextdinner.R

/**
 * Sealed class representing an icon resource.
 * It can be either a vector or a drawable resource.
 */
sealed class IconResource {
    /**
     * Data class representing a vector icon resource.
     *
     * @property vector The vector icon.
     */
    data class Vector(val vector: ImageVector) : IconResource()

    /**
     * Data class representing a drawable icon resource.
     *
     * @property resId The resource ID of the drawable icon.
     */
    data class Drawable(val resId: Int) : IconResource()
}

/**
 * Enum class representing the screens in the JochensNextDinner app.
 *
 * @property icon The icon resource for the screen.
 * @property label The resource ID of the label for the screen.
 * @property inBottomBar A flag indicating whether the screen is included in the bottom navigation bar.
 */
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

/**
 * Enum class representing the types of navigation in the JochensNextDinner app.
 */
enum class JndNavigationType {
    BOTTOM_NAVIGATION,
    PERMANENT_NAVIGATION_DRAWER
}