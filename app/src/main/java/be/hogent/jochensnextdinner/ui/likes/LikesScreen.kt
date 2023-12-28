package be.hogent.jochensnextdinner.ui.likes

import Like
import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.ui.components.LikeListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LikesScreen(
    likeViewModel: LikeViewModel = viewModel(factory = LikeViewModel.Factory),
) {
    val likeListState by likeViewModel.uiListState.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }
    val likeApiState = likeViewModel.likeApiState
    val lazyListState = rememberLazyListState()
    val isAddingVisible = mutableStateOf(false)
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            likeViewModel.refresh()
            isRefreshing.value = false
        }
    ) {
        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = {
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
            when (likeApiState) {
                is LikeApiState.Loading -> Text("Loading...")
                is LikeApiState.Error -> {
                    Column {
                        Text(likeApiState.message)
                        Button(onClick = {
                            likeViewModel.refresh()
                        }) {
                            Text("Try Again")
                        }
                    }
                }

                is LikeApiState.Success -> {
                    LazyColumn(state = lazyListState) {
                        val groupedLikes = likeListState.likeList.groupBy { it.category }
                        item {
                            if (likeListState.likeList.isEmpty() || isAddingVisible.value) {
                                LikeListItem(
                                    like = Like(name = "", category = ""),
                                    onSave = { like ->
                                        likeViewModel.saveLike(like)
                                        isAddingVisible.value = false
                                    },
                                    onDelete = {
                                        isAddingVisible.value = false
                                    },
                                )
                            }
                        }
                        groupedLikes.forEach { (category, likes) ->
                            stickyHeader {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(likes.size) { index ->
                                LikeListItem(
                                    like = likes[index],
                                    onSave = { likeViewModel.saveLike(it) },
                                    onDelete = { likeViewModel.deleteLike(it) },
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