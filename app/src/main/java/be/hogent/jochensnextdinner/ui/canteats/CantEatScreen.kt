import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.ui.canteats.CantEatApiState
import be.hogent.jochensnextdinner.ui.canteats.CantEatViewModel
import be.hogent.jochensnextdinner.ui.components.CantEatListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun CantEatScreen(
    modifier: Modifier = Modifier,
    cantEatViewModel: CantEatViewModel = viewModel(factory = CantEatViewModel.Factory),
    isAddNewVisible: Boolean = false,
    addNewVisibleReset: () -> Unit = {},
) {
    val cantEatListState by cantEatViewModel.uiListState.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }

    val cantEatApiState = cantEatViewModel.cantEatApiState

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            // Your refresh logic here
            isRefreshing.value = false
        }
    ) {

//            is CantEatUiState.Success -> {
//                LazyColumn {
//                    item {
//                        if (triggerAdd.value) {
//                            CantEatListItem(
//                                input = CantEatInput.New(),
//                                addNewVisibleReset = addNewVisibleReset,
//                                isAddingVisisble = isAddNewVisible
//                            )
//                        }
//                    }
//
//                    items(cantEatUiState.cantEats) { cantEat ->
//                        CantEatListItem(
//                            input = CantEatInput.Existing(cantEat),
//                            onSave = onSave,
//                            onDelete = onDelete
//                        )
//                    }

        Box(modifier = modifier) {
            when (cantEatApiState) {
                is CantEatApiState.Loading -> Text("Loading...")
                is CantEatApiState.Error -> Text("Couldn't load...")
                is CantEatApiState.Success -> {
                    LazyColumn {
                        item {
                            if (isAddNewVisible) {
                                CantEatListItem(
                                    cantEat = CantEat(name = ""),
                                    onSave = {/* onSave logic here */ },
                                    onDelete = {/* onDelete logic here */ }
                                )
                            }
                        }
                        items(cantEatListState.cantEatList) { cantEat ->
                            CantEatListItem(
                                cantEat = cantEat,
                                onSave = {},
                                onDelete = {/* onDelete logic here */ }
                            )
                        }
                    }

                }
            }

        }
    }
}


//@Composable
//fun CantEatListItem(
//    addNewVisibleReset: () -> Unit = {},
//    isAddingVisisble:  Boolean = false,
//) {
//    val text = remember {
//        mutableStateOf(
//            when (input) {
//                is CantEatInput.Existing -> input.cantEat.name
//                is CantEatInput.New -> ""
//            }
//        )
//    }
//    val isEditing = remember { mutableStateOf(input is CantEatInput.New) }
//
////TODO: add empty string validation
//
//    ListItem(
//        headlineContent = {
//            if (isEditing.value) {
//                BasicTextField(
//                    value = text.value,
//                    onValueChange = { text.value = it },
//                    singleLine = true,
//                    textStyle = MaterialTheme.typography.bodyLarge
//                )
//            } else {
//                Text(text = text.value, fontWeight = FontWeight.Bold)
//            }
//        },
//        colors = ListItemDefaults.colors(
//            containerColor = MaterialTheme.colorScheme.background,
//        ),
//        trailingContent = {
//            if (isEditing.value) {
//                Row {
//                    IconButton(onClick = {
//                        onSave(
//                            when (input) {
//                                is CantEatInput.Existing -> input.cantEat.copy(name = text.value)
////                                is CantEatInput.New -> CantEat()
//                                else -> throw IllegalStateException("Unexpected CantEatInput type")
//                            }
//                        )
//                        isEditing.value = false
//                        addNewVisibleReset()
//                    }) {
//                        Icon(
//                            Icons.Filled.Check,
//                            contentDescription = stringResource(id = R.string.save)
//                        )
//                    }
//                    IconButton(onClick = {
//                        if (text.value.isNotBlank()) { // Check for non-empty string
//                            onSave(
//                                when (input) {
//                                    is CantEatInput.Existing -> input.cantEat.copy(name = text.value)
////                                    is CantEatInput.New -> CantEat(name = text.value)
//                                    else -> throw IllegalStateException("Unexpected CantEatInput type")
//                                }f
//                            )
//                            isEditing.value = false
//                            addNewVisibleReset()
//                        }
//                    }) {
//                        Icon(Icons.Filled.Check, contentDescription = stringResource(id = R.string.save))
//                    }
//
//                }
//            } else {
//                IconButton(onClick = { isEditing.value = true }) {
//                    Icon(
//                        Icons.Filled.Edit,
//                        contentDescription = stringResource(id = androidx.compose.material3.R.string.m3c_bottom_sheet_expand_description)
//                    )
//                }
//            }
//        }
//    )
//}