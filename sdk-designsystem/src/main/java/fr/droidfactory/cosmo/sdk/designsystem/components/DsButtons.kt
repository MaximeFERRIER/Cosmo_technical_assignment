package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object DsButton {
    @Composable
    fun PrimaryButton(
        modifier: Modifier = Modifier,
        text: String,
        isLoading: Boolean = false,
        isEnabled: Boolean = true,
        iconStart: (@Composable () -> Unit)? = null,
        onClick: () -> Unit
    ) = DsButtonImpl(
        modifier = modifier,
        text = text,
        isLoading = isLoading,
        isEnabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ),
        textColor = MaterialTheme.colorScheme.onPrimary,
        iconStart = iconStart,
        onClick = onClick
    )

    @Composable
    fun TextButton(
        modifier: Modifier = Modifier,
        text: String,
        textColor: Color = MaterialTheme.colorScheme.primary,
        isLoading: Boolean = false,
        isEnabled: Boolean = true,
        iconStart: (@Composable () -> Unit)? = null,
        onClick: () -> Unit
    ) = DsButtonImpl(
        modifier = modifier,
        text = text,
        isLoading = isLoading,
        isEnabled = isEnabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = textColor,
            containerColor = Color.Transparent
        ),
        textColor = textColor,
        iconStart = iconStart,
        onClick = onClick
    )

    @Composable
    fun ErrorButton(
        modifier: Modifier = Modifier,
        text: String,
        textColor: Color = MaterialTheme.colorScheme.onError,
        isLoading: Boolean = false,
        isEnabled: Boolean = true,
        iconStart: (@Composable () -> Unit)? = null,
        onClick: () -> Unit
    ) = DsButtonImpl(
        modifier = modifier,
        text = text,
        isLoading = isLoading,
        isEnabled = isEnabled,
        textColor = textColor,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onError,
            containerColor = MaterialTheme.colorScheme.error
        ),
        iconStart = iconStart,
        onClick = onClick
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DsButtonImpl(
    modifier: Modifier,
    text: String,
    isLoading: Boolean,
    isEnabled: Boolean,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    textColor: Color,
    iconStart: (@Composable () -> Unit)?,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(56.dp),
        colors = colors,
        enabled = isEnabled,
        shape = MaterialTheme.shapes.medium,
        onClick = {
            if (!isLoading) {
                onClick()
            }
        }) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(16.dp),
                    strokeWidth = 2.dp,
                    color = textColor
                )
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                iconStart?.let {
                    it()
                    Spacer(modifier = Modifier.width(8.dp))
                }

                DsTexts.TextButton(
                    text = text,
                    color = textColor
                )
            }
        }
    }
}