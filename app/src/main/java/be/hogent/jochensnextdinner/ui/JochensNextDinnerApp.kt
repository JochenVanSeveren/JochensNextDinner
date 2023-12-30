package be.hogent.jochensnextdinner.ui

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import be.hogent.jochensnextdinner.ui.components.TopBar
import be.hogent.jochensnextdinner.ui.layout.BottomNavigationLayout
import be.hogent.jochensnextdinner.ui.layout.DrawerNavigationLayout
import be.hogent.jochensnextdinner.ui.navigation.NavComponent
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme
import be.hogent.jochensnextdinner.utils.JndNavigationType
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen


/*TODO: feed toevoegen */

@Composable
fun JochensNextDinnerApp(
    navController: NavHostController = rememberNavController(), navigationType: JndNavigationType,
) {
  

    when (navigationType) {
        JndNavigationType.BOTTOM_NAVIGATION -> {
            BottomNavigationLayout(navController)
        }

        JndNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            DrawerNavigationLayout(navController)
        }

        JndNavigationType.NAVIGATION_RAIL -> {
//            RailNavigationLayout(navController)
        }
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

//@Composable
//fun RailNavigationLayout(navController: NavHostController) {
//    Row(modifier = Modifier.fillMaxWidth()) {
//        NavigationRailComponent(navController = navController)
//        Scaffold(
//            topBar = { TopBar(navController = navController) }
//        ) { innerPadding ->
//            Surface(
//                modifier = Modifier.padding(innerPadding),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                NavComponent(navController = navController, modifier = Modifier.padding(16.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun NavigationRailComponent(navController: NavHostController ) {
//    NavigationRail {
//        NavigationRailItem(
//            icon = { Icon(Icons.Default.Home, contentDescription = "Start") },
//            label = { Text("Start") },
//            selected = (currentRoute == JochensNextDinnerScreen.Start.name),
//            onClick = { navController.navigate(JochensNextDinnerScreen.Start.name) }
//        )
//        NavigationRailItem(
//            icon = { Icon(Icons.Default.Home, contentDescription = "Can't Eat") },
//            label = { Text("Can't Eat") },
//            selected = (currentRoute == JochensNextDinnerScreen.CantEatScreen.name),
//            onClick = { navController.navigate(JochensNextDinnerScreen.CantEatScreen.name) }
//        )
//        // Add other NavigationRailItems here
//    }
//}


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

