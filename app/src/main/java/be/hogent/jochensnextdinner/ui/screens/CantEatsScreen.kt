package be.hogent.jochensnextdinner.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.ui.viewModels.CantEatUiState
import be.hogent.jochensnextdinner.ui.viewModels.CantEatViewModel

@Composable
fun CantEatScreen( cantEatUiState: CantEatUiState) {

    when (cantEatUiState) {
        is CantEatUiState.Loading -> {
            Text(text = "Loading...")
        }
        is CantEatUiState.Error -> {
            Text(text = CantEatUiState.Error.toString())
        }
        is CantEatUiState.Success -> {
            LazyColumn {
                items(cantEatUiState.cantEats) { cantEat ->
                    Text(text = cantEat.name)
                }
            }
        }
    }
}