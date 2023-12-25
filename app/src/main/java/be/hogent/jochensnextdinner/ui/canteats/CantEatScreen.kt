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
            cantEatViewModel.refresh();
            isRefreshing.value = false
        }
    ) {

        Box(modifier = modifier) {
            when (cantEatApiState) {
                is CantEatApiState.Loading -> Text("Loading...")
                is CantEatApiState.Error -> Text(cantEatApiState.message)
                is CantEatApiState.Success -> {
                    LazyColumn {
                        item {
                            if (isAddNewVisible) {
                                CantEatListItem(
                                    cantEat = CantEat(name = ""),
                                    onSave = { cantEat -> cantEatViewModel.saveCantEat(cantEat) },
                                    onDelete = { cantEat -> cantEatViewModel.deleteCantEat(cantEat) },
                                    addNewVisibleReset = addNewVisibleReset
                                )
                            }
                        }
                        items(cantEatListState.cantEatList) { cantEat ->
                            CantEatListItem(
                                cantEat = cantEat,
                                onSave = { cantEat -> cantEatViewModel.saveCantEat(cantEat) },
                                onDelete = { cantEat -> cantEatViewModel.deleteCantEat(cantEat) },
                                addNewVisibleReset = addNewVisibleReset
                            )
                        }
                    }

                }
            }

        }
    }
}

