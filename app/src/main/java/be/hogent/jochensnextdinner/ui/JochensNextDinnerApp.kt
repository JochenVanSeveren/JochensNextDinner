package be.hogent.jochensnextdinner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.ui.components.TopBar
import be.hogent.jochensnextdinner.ui.screens.CantEatScreen
import be.hogent.jochensnextdinner.ui.screens.LikesScreen
import be.hogent.jochensnextdinner.ui.screens.RecipesScreen
import be.hogent.jochensnextdinner.ui.screens.StartScreen
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme

enum class JochensNextDinnerScreen {
    Start,
    CantEatScreen,
    LikeScreen,
    RecipeScreen
}


@Composable
fun JochensNextDinnerApp(
    navController: NavHostController = rememberNavController()
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    val title = when (currentRoute) {
        JochensNextDinnerScreen.Start.name -> "Jochens Next Dinner"
        JochensNextDinnerScreen.CantEatScreen.name -> "Cant Eats"
        JochensNextDinnerScreen.LikeScreen.name -> "Likes"
        JochensNextDinnerScreen.RecipeScreen.name -> "Recipes"
        else -> ""
    }

    Scaffold(
        topBar = {
            TopBar(
                title,
                navController = navController
            )
        },
        bottomBar = {
            if (currentRoute == JochensNextDinnerScreen.Start.name) { // Show BottomAppBar only on Start screen
                BottomAppBar(
                    actions = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.navigate(JochensNextDinnerScreen.CantEatScreen.name) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.skull),
                                    contentDescription = "Nav button to canteats",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(onClick = { navController.navigate(JochensNextDinnerScreen.LikeScreen.name) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.thumb_up),
                                    contentDescription = "Nav button to likes",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(onClick = { navController.navigate(JochensNextDinnerScreen.RecipeScreen.name) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.skillet),
                                    contentDescription = "Nav button to recipes",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                )
            }
        },
        floatingActionButton = {
            if (currentRoute != JochensNextDinnerScreen.Start.name) { // Show FAB on all screens except Start
                FloatingActionButton(
                    onClick = { /* do something */ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(
                        Icons.Filled.Add,
                        "Localized description",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = JochensNextDinnerScreen.Start.name,
                modifier = Modifier.padding(16.dp)
            ) {
                composable(route = JochensNextDinnerScreen.Start.name) {
                    StartScreen(
                        onCantEatClick = { navController.navigate(JochensNextDinnerScreen.CantEatScreen.name) },
                        onLikeClick = { navController.navigate(JochensNextDinnerScreen.LikeScreen.name) },
                        onRecipeClick = { navController.navigate(JochensNextDinnerScreen.RecipeScreen.name) }
                    )
                }
                composable(route = JochensNextDinnerScreen.CantEatScreen.name) {
                    CantEatScreen()
                }
                composable(route = JochensNextDinnerScreen.LikeScreen.name) {
                    LikesScreen()
                }
                composable(route = JochensNextDinnerScreen.RecipeScreen.name) {
                    RecipesScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JochensNextDinnerAppPreview() {
    JochensNextDinnerTheme {
        JochensNextDinnerApp()
    }
}