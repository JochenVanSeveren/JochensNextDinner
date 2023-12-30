package be.hogent.jochensnextdinner.ui

import android.content.Intent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme

/**
 * Composable function for the StartScreen.
 * It displays clickable text and three IconTextRow items.
 *
 * @param onCantEatClick The function to be invoked when the CantEat IconTextRow is clicked.
 * @param onLikeClick The function to be invoked when the Like IconTextRow is clicked.
 * @param onRecipeClick The function to be invoked when the Recipe IconTextRow is clicked.
 */
@Composable
fun StartScreen(
    onCantEatClick: () -> Unit,
    onLikeClick: () -> Unit,
    onRecipeClick: () -> Unit,
) {
    val context = LocalContext.current

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

        // ClickableText with an annotated string. When the annotated part is clicked, it opens the URL in a browser.
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

        // IconTextRow for the CantEat item. When clicked, it invokes the onCantEatClick function.
        IconTextRow(
            onClick = onCantEatClick,
            iconId = R.drawable.skull,
            contentDescription = "CantEat",
            text = "Wat mag je niet eten?"
        )

        // IconTextRow for the Like item. When clicked, it invokes the onLikeClick function.
        IconTextRow(
            onClick = onLikeClick,
            iconId = R.drawable.thumb_up,
            contentDescription = "Like",
            text = "Wat mag je dan wel eten?"
        )

        // IconTextRow for the Recipe item. When clicked, it invokes the onRecipeClick function.
        IconTextRow(
            onClick = onRecipeClick,
            iconId = R.drawable.skillet,
            contentDescription = "Recipe",
            text = "Wat maak jij dan zoal?"
        )
    }
}

/**
 * Composable function for displaying a row with an icon and text.
 * The row is clickable and invokes the provided onClick function when clicked.
 *
 * @param onClick The function to be invoked when the row is clicked.
 * @param iconId The resource ID of the icon to display.
 * @param contentDescription The content description for the icon.
 * @param text The text to display.
 */
@Composable
fun IconTextRow(
    onClick: () -> Unit,
    iconId: Int,
    contentDescription: String,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 24.dp),
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
        Spacer(modifier = Modifier.width(16.dp))
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
        StartScreen(
            onCantEatClick = {},
            onLikeClick = {},
            onRecipeClick = {}
        )
    }
}