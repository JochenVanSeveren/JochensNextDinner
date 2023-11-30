package be.hogent.jochensnextdinner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import be.hogent.jochensnextdinner.ui.JochensNextDinnerApp
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JochensNextDinnerTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    JochensNextDinnerApp()
                }
            }
        }
    }
}
