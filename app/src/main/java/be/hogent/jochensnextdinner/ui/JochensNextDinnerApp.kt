package be.hogent.jochensnextdinner.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import be.hogent.jochensnextdinner.ui.layout.BottomNavigationLayout
import be.hogent.jochensnextdinner.ui.layout.DrawerNavigationLayout
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme
import be.hogent.jochensnextdinner.utils.JndNavigationType


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
    }


}



@Preview(showBackground = true)
@Composable
fun JochensNextDinnerAppPreviewBOTTOM_NAVIGATION() {
    JochensNextDinnerTheme {
        JochensNextDinnerApp(navigationType = JndNavigationType.BOTTOM_NAVIGATION)
    }
}


@Preview(showBackground = true, widthDp = 1000)
@Composable
fun JochensNextDinnerAppPreviewPERMANENT_NAVIGATION_DRAWER() {
    JochensNextDinnerTheme() {
        JochensNextDinnerApp(navigationType = JndNavigationType.PERMANENT_NAVIGATION_DRAWER)
    }
}

