package be.hogent.jochensnextdinner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

@Composable
fun BottomBar(
    navController: NavHostController,
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
        .filter { it.inBottomBar }
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                IconButton(onClick = { navController.navigate(screen.name) }) {
                    if (screen.vectorIcon != null) {
                        Icon(
                            imageVector = screen.vectorIcon,
                            contentDescription = stringResource(id = screen.label),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    } else if (screen.iconResource != null) {
                        Icon(
                            painter = painterResource(id = screen.iconResource),
                            contentDescription = stringResource(id = screen.label),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

