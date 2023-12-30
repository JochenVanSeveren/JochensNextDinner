package be.hogent.jochensnextdinner.ui.appSections.likes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.ui.components.LikeListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Composable function for the LikesScreen.
 *
 * @param likeViewModel The ViewModel for managing Like data.
 */
@Composable
fun LikesScreen(
    likeViewModel: LikeViewModel = viewModel(factory = LikeViewModel.Factory),
) {
    // State for the list of Like items
    val likeListState by likeViewModel.uiListState.collectAsState()
    // State for the refreshing status
    val isRefreshing = remember { mutableStateOf(false) }
    // State for the API status
    val likeApiState = likeViewModel.likeApiState

    // SwipeRefresh layout for refreshing the list of Like items
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            likeViewModel.refresh()
            isRefreshing.value = false
        }
    ) {
        // Box layout for the screen
        Box(modifier = Modifier.fillMaxSize()) {

            // Display different UI based on the API status
            when (likeApiState) {
                // Display a CircularProgressIndicator when loading
                is LikeApiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                // Display an error message and a retry button when an error occurs
                is LikeApiState.Error -> {
                    Column {
                        Text(likeApiState.message)
                        Button(onClick = {
                            likeViewModel.refresh()
                        }) {
                            Text(stringResource(R.string.try_again))
                        }
                    }
                }

                // Display a list of Like items when the data is successfully fetched
                is LikeApiState.Success -> {
                    LazyColumn {
                        // Group the Like items by category
                        val groupedLikes = likeListState.likeList.groupBy { it.category }
                        groupedLikes.forEach { (category, likes) ->
                            // Display the category as a header
                            item {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Display the Like items for the category
                            items(likes.size) { index ->
                                LikeListItem(
                                    like = likes[index]
                                )
                            }
                        }
                        // Add a spacer at the end of the list
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}