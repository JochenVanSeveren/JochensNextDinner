package be.hogent.jochensnextdinner.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
import be.hogent.jochensnextdinner.ui.viewModels.CantEatViewModel

enum class JochensNextDinnerScreen {
    Start,
    CantEatScreen,
    LikeScreen,
    RecipeScreen
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JochensNextDinnerApp(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    val title = when (currentRoute) {
        JochensNextDinnerScreen.Start.name ->   context.getString(R.string.app_name)
        JochensNextDinnerScreen.CantEatScreen.name ->  context.getString(R.string.cant_eat_screen)
        JochensNextDinnerScreen.LikeScreen.name ->  context.getString(R.string.like_screen)
        JochensNextDinnerScreen.RecipeScreen.name ->  context.getString(R.string.recipe_screen)
        else -> ""
    }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                title,
                navController = navController
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = currentRoute == JochensNextDinnerScreen.Start.name,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                )
            ) {
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
                                    contentDescription = context.getString(R.string.navigate_to_cant_eat_screen),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(onClick = { navController.navigate(JochensNextDinnerScreen.LikeScreen.name) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.thumb_up),
                                    contentDescription = context.getString(R.string.navigate_to_like_screen),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(onClick = { navController.navigate(JochensNextDinnerScreen.RecipeScreen.name) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.skillet),
                                    contentDescription = context.getString(R.string.navigate_to_recipe_screen),
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
            AnimatedVisibility(
                visible = currentRoute != JochensNextDinnerScreen.Start.name,
                enter = fadeIn(animationSpec = tween(durationMillis = 3500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                FloatingActionButton(
                    onClick = { /* do something */ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(
                        Icons.Filled.Add,
                        "Add button",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it), color = MaterialTheme.colorScheme.background) {
            val cantEatViewModel: CantEatViewModel = viewModel(factory = CantEatViewModel.Factory)
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
                    CantEatScreen(cantEatViewModel.cantEatUiState)
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