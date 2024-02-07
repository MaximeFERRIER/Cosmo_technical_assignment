package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

object DsSnackbars {
    @Composable
    fun SnackbarError(
        modifier: Modifier = Modifier,
        message: String
    ) = SnackbarImpl(
        modifier = modifier,
        message = message,
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError
    )

    @Composable
    fun SnackbarSuccess(
        modifier: Modifier = Modifier,
        message: String
    ) = SnackbarImpl(
        modifier = modifier,
        message = message,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
private fun SnackbarImpl(
    modifier: Modifier,
    message: String,
    containerColor: Color,
    contentColor: Color
) {
    Snackbar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        DsTexts.BodyLarge(title = message, color = contentColor)
    }
}