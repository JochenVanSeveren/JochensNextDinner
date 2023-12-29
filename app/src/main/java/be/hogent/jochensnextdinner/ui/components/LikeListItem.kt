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

@Composable
fun LikeListItem(
    like: Like,
) {
    val name = remember { mutableStateOf(like.name) }

    ListItem(
        headlineContent = {
            Column {
                Text(text = name.value, fontWeight = FontWeight.Bold)
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}