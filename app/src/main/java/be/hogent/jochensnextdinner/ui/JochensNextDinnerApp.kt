package be.hogent.jochensnextdinner.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.ui.components.BottomBar
import be.hogent.jochensnextdinner.ui.components.TopBar
import be.hogent.jochensnextdinner.ui.navigation.NavComponent
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme
import be.hogent.jochensnextdinner.utils.JndNavigationType
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen


/*TODO: feed toevoegen */

@Composable
fun JochensNextDinnerApp(
    navController: NavHostController = rememberNavController(), navigationType: JndNavigationType,
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    val title = when (currentRoute) {
        JochensNextDinnerScreen.CantEatScreen.name -> stringResource(id = R.string.cant_eat_screen)
        JochensNextDinnerScreen.LikeScreen.name -> stringResource(id = R.string.like_screen)
        JochensNextDinnerScreen.RecipeScreen.name -> stringResource(id = R.string.recipe_screen)
        "RecipeDetailScreen/{recipeId}?title={title}" -> currentDestination?.arguments?.getString("title")
            ?: stringResource(id = R.string.app_name)

        else -> stringResource(id = R.string.app_name)
    }

    when (navigationType) {
        JndNavigationType.BOTTOM_NAVIGATION -> {
            BottomNavigationLayout(title, navController, currentRoute)
        }

        JndNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            DrawerNavigationLayout(title, navController, currentRoute)
        }

        JndNavigationType.NAVIGATION_RAIL -> {
            RailNavigationLayout(title, navController, currentRoute)
        }
    }


}

@Composable
fun BottomNavigationLayout(
    title: String,
    navController: NavHostController,
    currentRoute: String?,
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
        .filter { it.inBottomBar }
) {
    Scaffold(
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
                BottomBar(navController, screens)
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it), color = MaterialTheme.colorScheme.background) {
            NavComponent(
                navController = navController,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun DrawerNavigationLayout(title: String, navController: NavHostController, currentRoute: String?) {
    Scaffold(
        topBar = { TopBar(title, navController = navController) },
//        drawerContent = { DrawerContent(navController) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavComponent(navController = navController, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController) {
    // Implementation for each item in the drawer
    Column {
        DrawerItem(
            "Start",
            Icons.Default.Home
        ) { navController.navigate(JochensNextDinnerScreen.Start.name) }
        DrawerItem(
            "Can't Eat",
            Icons.Default.Home
        ) { navController.navigate(JochensNextDinnerScreen.CantEatScreen.name) }
        DrawerItem(
            "Likes",
            Icons.Default.Home
        ) { navController.navigate(JochensNextDinnerScreen.LikeScreen.name) }
        DrawerItem(
            "Recipes",
            Icons.Default.Home
        ) { navController.navigate(JochensNextDinnerScreen.RecipeScreen.name) }
        // Add other drawer items here
    }
}

@Composable
fun DrawerItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = text)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun RailNavigationLayout(title: String, navController: NavHostController, currentRoute: String?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        NavigationRailComponent(navController = navController, currentRoute = currentRoute)
        Scaffold(
            topBar = { TopBar(title, navController = navController) }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavComponent(navController = navController, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun NavigationRailComponent(navController: NavHostController, currentRoute: String?) {
    NavigationRail {
        NavigationRailItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Start") },
            label = { Text("Start") },
            selected = (currentRoute == JochensNextDinnerScreen.Start.name),
            onClick = { navController.navigate(JochensNextDinnerScreen.Start.name) }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Can't Eat") },
            label = { Text("Can't Eat") },
            selected = (currentRoute == JochensNextDinnerScreen.CantEatScreen.name),
            onClick = { navController.navigate(JochensNextDinnerScreen.CantEatScreen.name) }
        )
        // Add other NavigationRailItems here
    }
}


@Preview(showBackground = true)
@Composable
fun JochensNextDinnerAppPreviewBOTTOM_NAVIGATION() {
    JochensNextDinnerTheme {
        JochensNextDinnerApp(navigationType = JndNavigationType.BOTTOM_NAVIGATION)
    }
}


@Preview(showBackground = true, widthDp = 800)
@Composable
fun JochensNextDinnerAppPreviewNAVIGATION_RAIL() {
    JochensNextDinnerTheme() {
        JochensNextDinnerApp(navigationType = JndNavigationType.NAVIGATION_RAIL)
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun JochensNextDinnerAppPreviewPERMANENT_NAVIGATION_DRAWER() {
    JochensNextDinnerTheme() {
        JochensNextDinnerApp(navigationType = JndNavigationType.PERMANENT_NAVIGATION_DRAWER)
    }
}

