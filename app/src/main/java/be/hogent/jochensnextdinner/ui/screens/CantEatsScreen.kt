import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.ui.viewModels.CantEatUiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun CantEatScreen(
    cantEatUiState: CantEatUiState,
    onAdd: (CantEat) -> Unit,
    onSave: (CantEat) -> Unit,
    onDelete: (CantEat) -> Unit,
    onRefresh: () -> Unit,
    triggerAdd: MutableState<Boolean>,
) {
    val isRefreshing = remember { mutableStateOf(false) }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            onRefresh.invoke()
            isRefreshing.value = false
        }
    ) {
        when (cantEatUiState) {
            is CantEatUiState.Loading -> {
                Text(text = "Loading...")
            }

            is CantEatUiState.Error -> {
                Text(text = cantEatUiState.message)
            }

            is CantEatUiState.Success -> {
                LazyColumn {
                    item {
                        if (triggerAdd.value) {
                            CantEatListItem(
                                cantEat = CantEat(name = ""),
                                onSave = onAdd,
                                onDelete = {}
                            )
                            triggerAdd.value = false
                        }
                    }
                    items(cantEatUiState.cantEats) { cantEat ->
                        CantEatListItem(
                            cantEat = cantEat,
                            onSave = onSave,
                            onDelete = onDelete
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CantEatListItem(
    cantEat: CantEat,
    onSave: (CantEat) -> Unit,
    onDelete: (CantEat) -> Unit
) {
    val text = remember { mutableStateOf(cantEat.name) }
    val isEditing = remember { mutableStateOf(cantEat.name.isEmpty()) }

    ListItem(
        headlineContent = {
            if (isEditing.value) {
                BasicTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(text = text.value, fontWeight = FontWeight.Bold)
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        trailingContent = {
            if (isEditing.value) {
                Row {
                    IconButton(onClick = {
                        onSave(cantEat.copy(name = text.value))
                        isEditing.value = false
                    }) {
                        Icon(Icons.Filled.Check, contentDescription = "Save")
                    }
                    IconButton(onClick = {
                        onDelete(cantEat)
                        isEditing.value = false
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            } else {
                IconButton(onClick = { isEditing.value = true }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            }
        }
    )
}