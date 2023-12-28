import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.ui.canteats.CantEatApiState
import be.hogent.jochensnextdinner.ui.canteats.CantEatViewModel
import be.hogent.jochensnextdinner.ui.components.CantEatListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CantEatScreen(
    cantEatViewModel: CantEatViewModel = viewModel(factory = CantEatViewModel.Factory),
) {
    val cantEatListState by cantEatViewModel.uiListState.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }
    val cantEatApiState = cantEatViewModel.cantEatApiState
    val lazyListState = rememberLazyListState()
    val isAddingVisible = mutableStateOf(false)
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            cantEatViewModel.refresh()
            isRefreshing.value = false
        }
    ) {
        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                cantEatViewModel.viewModelScope.launch {
//                    lazyListState.animateScrollToItem(0)
//                }
//                cantEatViewModel.toggleAddNew()
                    isAddingVisible.value = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Add button",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }) {


            when (cantEatApiState) {
                is CantEatApiState.Loading -> Text("Loading...")
                is CantEatApiState.Error -> {
                    Column {
                        Text(cantEatApiState.message)
                        Button(onClick = {
                            cantEatViewModel.refresh()
                        }) {
                            Text("Try Again")
                        }
                    }
                }

                is CantEatApiState.Success -> {
                    LazyColumn(state = lazyListState) {
                        item {
                            if (cantEatListState.cantEatList.isEmpty() || isAddingVisible.value) {
                                CantEatListItem(
                                    cantEat = CantEat(name = ""),
                                    onSave = { cantEat ->
                                        cantEatViewModel.saveCantEat(cantEat)
                                        isAddingVisible.value = false
                                    },
                                    onDelete = {
                                        isAddingVisible.value = false
                                    },
                                )
                            }
                        }
                        items(cantEatListState.cantEatList) { cantEat ->
                            CantEatListItem(
                                cantEat = cantEat,
                                onSave = { cantEatViewModel.saveCantEat(it) },
                                onDelete = { cantEatViewModel.deleteCantEat(it) },
                            )
                        }
                    }

                }
            }
        }
    }
}