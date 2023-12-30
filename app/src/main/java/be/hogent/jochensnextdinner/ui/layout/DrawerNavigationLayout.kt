package be.hogent.jochensnextdinner.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.ui.components.TopBar
import be.hogent.jochensnextdinner.ui.navigation.NavComponent
import be.hogent.jochensnextdinner.utils.IconResource
import be.hogent.jochensnextdinner.utils.JndNavigationType
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

@Composable
fun DrawerNavigationLayout(
    navController: NavHostController,
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                Modifier
                    .width(dimensionResource(R.dimen.drawer_width))
            ) {
                DrawerContent(navController, screens)
            }
        }) {
        Scaffold(
            topBar = { TopBar(navController = navController, showGoBackButton = false) },
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavComponent(navController = navController, modifier = Modifier.padding(16.dp), navigationType = JndNavigationType.PERMANENT_NAVIGATION_DRAWER)
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavHostController,
    screens: List<JochensNextDinnerScreen>
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(top = 65.dp)
            .fillMaxHeight()
    ) {
        for (navItem in screens) {
            NavigationDrawerItem(
                selected = navController.currentDestination?.route == navItem.name,
                label = {
                    Text(
                        text = stringResource(id = navItem.label),
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.drawer_padding_header)),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                icon = {
                    when (val icon = navItem.icon) {
                        is IconResource.Vector -> {
                            Icon(
                                imageVector = icon.vector,
                                contentDescription = stringResource(id = navItem.label),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        is IconResource.Drawable -> {
                            Icon(
                                painter = painterResource(id = icon.resId),
                                contentDescription = stringResource(id = navItem.label),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                ),
                onClick = { navController.navigate(navItem.name) },
            )
        }
    }
}