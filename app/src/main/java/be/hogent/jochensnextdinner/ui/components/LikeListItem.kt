package be.hogent.jochensnextdinner.ui.components

import Like
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight

/**
 * Composable function for displaying a list item for a Like object.
 *
 * @param like The Like object to display.
 */
@Composable
fun LikeListItem(
    like: Like,
) {
    // Remember the name of the Like object for displaying
    val name = remember { mutableStateOf(like.name) }

    // Create a ListItem with the name of the Like object
    ListItem(
        headlineContent = {
            Column {
                // Display the name of the Like object in bold text
                Text(text = name.value, fontWeight = FontWeight.Bold)
            }
        },
        // Set the colors for the ListItem
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}