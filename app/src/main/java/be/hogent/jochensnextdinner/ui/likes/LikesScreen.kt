package be.hogent.jochensnextdinner.ui.likes

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

@Composable
fun LikesScreen(
    likeViewModel: LikeViewModel = viewModel(factory = LikeViewModel.Factory),
) {
    val likeListState by likeViewModel.uiListState.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }
    val likeApiState = likeViewModel.likeApiState

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            likeViewModel.refresh()
            isRefreshing.value = false
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            when (likeApiState) {
                is LikeApiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

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

                is LikeApiState.Success -> {
                    LazyColumn {
                        val groupedLikes = likeListState.likeList.groupBy { it.category }
                        groupedLikes.forEach { (category, likes) ->
                            item {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(likes.size) { index ->
                                LikeListItem(
                                    like = likes[index]
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }


        }
    }
}