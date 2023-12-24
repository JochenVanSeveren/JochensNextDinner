package be.hogent.jochensnextdinner.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import be.hogent.jochensnextdinner.model.CantEat

@Composable
fun CantEatListItem(
    cantEat: CantEat,
    onSave: (CantEat) -> Unit,
    onDelete: (CantEat) -> Unit,
    addNewVisibleReset: () -> Unit = {},
) {
    val isNew = cantEat.name.isEmpty()
    val isEditing = remember { mutableStateOf(isNew) }
    val name = remember { mutableStateOf(cantEat.name) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isEditing.value) {
        if (isEditing.value) {
            focusRequester.requestFocus()
        }
    }

    ListItem(
        headlineContent = {
            if (isEditing.value) {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.focusRequester(focusRequester)
                )
            } else {
                Text(text = name.value, fontWeight = FontWeight.Bold)
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        trailingContent = {
            if (isEditing.value) {
                Row {
                    IconButton(onClick = {
                        Log.d("new name", name.value)
                        onSave(cantEat.copy(name = name.value))
                        isEditing.value = false
                    }) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.save)
                        )
                    }
                    IconButton(onClick = {
                        if (!isNew)
                            onDelete(cantEat)
                        else
                            addNewVisibleReset()
                        isEditing.value = false
                    }) {
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
                        Icons.Filled.Edit,
                        contentDescription = stringResource(id = androidx.compose.material3.R.string.m3c_bottom_sheet_expand_description)
                    )
                }
            }
        })
}