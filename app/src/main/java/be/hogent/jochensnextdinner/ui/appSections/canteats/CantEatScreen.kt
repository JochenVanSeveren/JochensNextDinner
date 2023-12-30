import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatApiState
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatViewModel
import be.hogent.jochensnextdinner.ui.components.CantEatListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Composable function for the CantEatScreen.
 *
 * @param cantEatViewModel The ViewModel for managing CantEat data.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CantEatScreen(
    cantEatViewModel: CantEatViewModel = viewModel(factory = CantEatViewModel.Factory),
) {
    // State for the list of CantEat items
    val cantEatListState by cantEatViewModel.uiListState.collectAsState()
    // State for the refreshing status
    val isRefreshing = remember { mutableStateOf(false) }
    // State for the API status
    val cantEatApiState = cantEatViewModel.cantEatApiState
    // State for the visibility of the adding form
    val isAddingVisible = mutableStateOf(false)

    // SwipeRefresh layout for refreshing the list of CantEat items
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            cantEatViewModel.refresh()
            isRefreshing.value = false
        }
    ) {
        // Scaffold layout for the screen
        Scaffold(floatingActionButton = {
            // FloatingActionButton for adding a new CantEat item
            FloatingActionButton(
                onClick = {
                    isAddingVisible.value = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    Icons.Filled.Add,
                    stringResource(R.string.add_button),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }) {
            // Display different UI based on the API status
            when (cantEatApiState) {
                // Display a CircularProgressIndicator when loading
                is CantEatApiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                // Display an error message and a retry button when an error occurs
                is CantEatApiState.Error -> {
                    Column {
                        Text(cantEatApiState.message)
                        Button(onClick = {
                            cantEatViewModel.refresh()
                        }) {
                            Text(stringResource(R.string.try_again))
                        }
                    }
                }

                // Display a list of CantEat items when the data is successfully fetched
                is CantEatApiState.Success -> {
                    LazyColumn() {
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