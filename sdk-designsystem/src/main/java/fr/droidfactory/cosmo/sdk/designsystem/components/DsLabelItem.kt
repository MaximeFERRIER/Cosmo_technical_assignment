package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

object DsLabelItem {
    @Composable
    fun LabelText(
        modifier: Modifier = Modifier,
        title: String,
        text: String,
        isFirstItemOfTheList: Boolean,
        isLastItemOfTheList: Boolean
    ) = LabelItemImpl(
        modifier = modifier,
        title = title,
        content = { DsTexts.BodyLarge(title = text, maxLines = 1, color = MaterialTheme.colorScheme.secondary) },
        isFirstItemOfTheList = isFirstItemOfTheList,
        isLastItemOfTheList = isLastItemOfTheList
    )

    @Composable
    fun LabelCustom(
        modifier: Modifier = Modifier,
        title: String,
        content: @Composable () -> Unit,
        isFirstItemOfTheList: Boolean,
        isLastItemOfTheList: Boolean
    )  = LabelItemImpl(
        modifier = modifier,
        title = title,
        content = content,
        isFirstItemOfTheList = isFirstItemOfTheList,
        isLastItemOfTheList = isLastItemOfTheList
    )
}
@Composable
private fun LabelItemImpl(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
    isFirstItemOfTheList: Boolean,
    isLastItemOfTheList: Boolean
) {
    val topBorderShape = if(isFirstItemOfTheList) 16.dp else 0.dp
    val bottomBorderShape = if(isLastItemOfTheList) 16.dp else 0.dp

    Surface(
        modifier = modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(topStart = topBorderShape, topEnd = topBorderShape, bottomStart = bottomBorderShape, bottomEnd = bottomBorderShape),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
            DsTexts.BodyLarge(title = title, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.width(8.dp))
            content()
        }
    }
}