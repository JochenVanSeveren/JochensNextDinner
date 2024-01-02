package be.hogent.jochensnextdinner.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
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
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

@Composable
fun StartScreen(
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
        .filter { it.inBottomBar },
    onScreenClick: (JochensNextDinnerScreen) -> Unit,
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

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            screens.forEach { screen ->
                createIconTextRow(screen, context, onScreenClick, orientation)
            }
        } else {
            Row {
                screens.forEach { screen ->
                    createIconTextRow(screen, context, onScreenClick, orientation)
                }
            }
        }
    }
}

@Composable
fun createIconTextRow(
    screen: JochensNextDinnerScreen,
    context: Context,
    onScreenClick: (JochensNextDinnerScreen) -> Unit,
    orientation: Int
) {
    val text = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
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
            text = text
        )
    }
}

@Composable
fun IconTextRow(
    onClick: () -> Unit,
    iconId: Int,
    contentDescription: String,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 12.dp), // Reduced padding
    ) {

        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp, 15.dp, 24.dp, 0.dp))
                .background(MaterialTheme.colorScheme.primary),
            onClick = onClick,
            content = {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp)) // Reduced width
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
        StartScreen(onScreenClick = { })
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun StartScreenPreviewHorizontal() {
    JochensNextDinnerTheme {
        StartScreen(onScreenClick = { })
    }
}