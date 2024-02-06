package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object DsFab {
    @Composable
    fun SecondaryFab(
        text: String,
        icon: ImageVector,
        onClick: () -> Unit
    ) = DsFabImpl(
        text = text,
        icon = icon,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        onClick = onClick
    )
}

@Composable
private fun DsFabImpl(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    ExtendedFloatingActionButton(
        text = { DsTexts.BodyMedium(title = text) }, icon = {
            Icon(
                imageVector = icon,
                contentDescription = ""
            )
        }, onClick = { onClick() },
        containerColor = containerColor,
        contentColor = contentColor
    )
}