package be.hogent.jochensnextdinner.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme
import be.hogent.jochensnextdinner.utils.IconResource
import be.hogent.jochensnextdinner.utils.JndNavigationType
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen


/**
 * Composable function for the StartScreen.
 * It displays clickable text and a list of IconTextRow items.
 * The items are displayed in a column in portrait mode and in a row in landscape mode.
 * The text for each item is determined based on the orientation and the navigation type.
 * If the orientation is portrait or the navigation type is PERMANENT_NAVIGATION_DRAWER, the long description is shown.
 * Otherwise, the label is shown.
 *
 * @param screens The list of screens to display. By default, it includes all screens that have the inBottomBar property set to true.
 * @param onScreenClick The function to be invoked when a screen is clicked.
 * @param navigationType The type of navigation to use (e.g., bottom navigation, drawer navigation).
 */
@Composable
fun StartScreen(
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
        .filter { it.inBottomBar },
    onScreenClick: (JochensNextDinnerScreen) -> Unit,
    navigationType: JndNavigationType
) {
    val context = LocalContext.current
    val orientation = context.resources.configuration.orientation

    Column {
        val annotatedText = buildAnnotatedString {
            append("Met mijn darmaandoening (")
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                append("pbs")
            }
            append(") krijg ik vaak volgende vragen")
            addStringAnnotation(
                tag = "URL",
                annotation = "https://www.gezondheidenwetenschap.be/richtlijnen/prikkelbaredarmsyndroom-pds",
                start = 23,
                end = 26
            )
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(annotation.item)
                            )
                        )
                    }
            },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        if (orientation == Configuration.ORIENTATION_PORTRAIT || navigationType == JndNavigationType.PERMANENT_NAVIGATION_DRAWER) {
            screens.forEach { screen ->
                createIconTextRow(screen, context, onScreenClick, showLongDescription = true)
            }
        } else {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                screens.forEach { screen ->
                    createIconTextRow(screen, context, onScreenClick, showLongDescription = false)
                }
            }
        }
    }
}

/**
 * Composable function for creating an IconTextRow.
 * It creates an IconTextRow for a given screen.
 * The text for the IconTextRow is determined based on the showLongDescription parameter.
 * If showLongDescription is true, the long description is shown.
 * Otherwise, the label is shown.
 *
 * @param screen The screen for which to create the IconTextRow.
 * @param context The local context.
 * @param onScreenClick The function to be invoked when the screen is clicked.
 * @param showLongDescription A flag indicating whether to show the long description or the label.
 */
@Composable
fun createIconTextRow(
    screen: JochensNextDinnerScreen,
    context: Context,
    onScreenClick: (JochensNextDinnerScreen) -> Unit,
    showLongDescription : Boolean
) {
    val text = if (showLongDescription) {
        context.getString(screen.description)
    } else {
        context.getString(screen.label)
    }

    when (val icon = screen.icon) {
        is IconResource.Drawable -> icon.resId
        else -> null
    }?.let {
        IconTextRow(
            onClick = { onScreenClick(screen) },
            iconId = it,
            contentDescription = context.getString(screen.label),
            text = text,
            testTag = context.getString(screen.label) + "NavIcon"
        )
    }
}

/**
 * Composable function for creating an IconTextRow.
 * It creates an IconTextRow for a given screen.
 *
 * @param onClick The function to be invoked when the screen is clicked.
 * @param iconId The resource ID of the icon.
 * @param contentDescription The content description of the icon.
 * @param text The text to display.
 * @param testTag The test tag for the IconTextRow.
 */
@Composable
fun IconTextRow(
    onClick: () -> Unit,
    iconId: Int,
    contentDescription: String,
    text: String,
    testTag: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 12.dp)
            .width(300.dp)
    ) {

        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp, 15.dp, 24.dp, 0.dp))
                .background(MaterialTheme.colorScheme.primary)
                .testTag(testTag),
            onClick = onClick,
            content = {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    JochensNextDinnerTheme {
        StartScreen(onScreenClick = { }, navigationType = JndNavigationType.BOTTOM_NAVIGATION)
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun StartScreenPreviewHorizontal() {
    JochensNextDinnerTheme {
        StartScreen(
            onScreenClick = { },
            navigationType = JndNavigationType.BOTTOM_NAVIGATION
        )
    }
}

@Preview(showBackground = true, widthDp = 900, heightDp = 500)
@Composable
fun StartScreenPreviewHorizontalBigScreen() {
    JochensNextDinnerTheme {
        StartScreen(
            onScreenClick = { },
            navigationType = JndNavigationType.PERMANENT_NAVIGATION_DRAWER
        )
    }
}