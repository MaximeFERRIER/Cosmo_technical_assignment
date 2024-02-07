package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

object DsTexts {
    @Composable
    fun TextButton(
        modifier: Modifier = Modifier,
        text: String,
        color: Color = MaterialTheme.colorScheme.primary,
        align: TextAlign = TextAlign.Start
    ) = DsTextsImpl(
        modifier = modifier,
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        align = align,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

    @Composable
    fun TitleMedium(
        modifier: Modifier = Modifier,
        title: String,
        align: TextAlign = TextAlign.Start,
        color: Color = MaterialTheme.colorScheme.onBackground,
        maxLines: Int = 1
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.titleMedium,
        align = align,
        maxLines = maxLines
    )

    @Composable
    fun TitleSmall(
        modifier: Modifier = Modifier,
        title: String,
        align: TextAlign = TextAlign.Start
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.headlineSmall,
        align = align,
        maxLines = 1
    )

    @Composable
    fun BodyLarge(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.onBackground,
        align: TextAlign = TextAlign.Start,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        align = align,
        overflow = overflow,
        maxLines = maxLines
    )

    @Composable
    fun BodyMedium(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.onBackground,
        align: TextAlign = TextAlign.Start,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.bodyMedium,
        align = align,
        overflow = overflow,
        maxLines = maxLines
    )

    @Composable
    fun BodySmall(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.onBackground,
        align: TextAlign = TextAlign.Start,
        maxLines: Int = Int.MAX_VALUE
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.bodySmall,
        align = align,
        maxLines = maxLines
    )

    @Composable
    fun HeadlineSmall(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.onBackground,
        align: TextAlign = TextAlign.Start,
        maxLines: Int = 1
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.headlineSmall,
        align = align,
        maxLines = maxLines
    )

    @Composable
    fun HeadlineMedium(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.onBackground,
        align: TextAlign = TextAlign.Start,
        maxLines: Int = 1
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.headlineMedium,
        align = align,
        maxLines = maxLines
    )

    @Composable
    fun HeadlineLarge(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.onBackground,
        align: TextAlign = TextAlign.Start,
        maxLines: Int = 1
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.headlineLarge,
        align = align,
        maxLines = maxLines
    )

    @Composable
    fun Link(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.onBackground,
        align: TextAlign = TextAlign.Start,
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.bodyMedium,
        align = align,
        maxLines = 1
    )

    @Composable
    fun InlineMessage(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = MaterialTheme.colorScheme.tertiary,
        align: TextAlign = TextAlign.Start,
        maxLines: Int = 1
    ) = DsTextsImpl(
        modifier = modifier,
        text = title,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        align = align,
        maxLines = maxLines
    )
}

@Composable
private fun DsTextsImpl(
    modifier: Modifier,
    text: String,
    color: Color,
    style: TextStyle,
    align: TextAlign = TextAlign.Start,
    maxLines: Int,
    overflow: TextOverflow = TextOverflow.Clip,
    textDecoration: TextDecoration = TextDecoration.None
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        color = color,
        textAlign = align,
        maxLines = maxLines,
        overflow = overflow,
        textDecoration = textDecoration,

        )
}