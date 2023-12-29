package be.hogent.jochensnextdinner.ui.components

import Like
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import be.hogent.jochensnextdinner.R

@Composable
fun LikeListItem(
    like: Like,
    onSave: (Like) -> Unit,
    onDelete: (Like) -> Unit,
) {
    val isNew = like.name.isEmpty()
    val isEditing = remember { mutableStateOf(isNew) }
    val name = remember { mutableStateOf(like.name) }
    val category = remember { mutableStateOf(like.category) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isEditing.value) {
        if (isEditing.value) {
            focusRequester.requestFocus()
        }
    }

    ListItem(
        headlineContent = {
            if (isEditing.value) {
                Column {
                    TextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.focusRequester(focusRequester),
                        placeholder = { Text(text = "Name") }
                    )
                    TextField(
                        value = category.value,
                        onValueChange = { category.value = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        placeholder = { Text(text = "Category") }
                    )
                }
            } else {
                Column {
                    Text(text = name.value, fontWeight = FontWeight.Bold)
                }
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        trailingContent = {
            if (isEditing.value) {
                Row {
                    IconButton(onClick = {
                        onSave(like.copy(name = name.value, category = category.value))
                        if (isNew) {
                            name.value = ""
                            category.value = ""
                        }
                        isEditing.value = false
                    }) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.save)
                        )
                    }
                    IconButton(onClick = {
                        if (!isNew) {
                            onDelete(like)
                        } else {
                            isEditing.value = false
                        }
                    }) {
                        if (isNew)
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = stringResource(id = R.string.cancel)
                            )
                        else
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = stringResource(id = R.string.delete)
                            )
                    }
                }
            } else {
                IconButton(onClick = {
                    isEditing.value = true
                }) {
                    Icon(
                        Icons.Filled.Edit, contentDescription = stringResource(id = R.string.edit)
                    )
                }
            }
        })
}