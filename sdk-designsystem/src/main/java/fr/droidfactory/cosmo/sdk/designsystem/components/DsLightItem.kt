package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

object DsLightItem {
    @Composable
    fun OffLight(
        modifier: Modifier = Modifier
    ) = DsLightItemImpl(
        modifier = modifier,
        color = Color.Gray
    )

    @Composable
    fun BothLight(
        modifier: Modifier = Modifier
    ) = DsLightItemImpl(
        modifier = modifier,
        color = Color.Green
    )

    @Composable
    fun PositionLight(
        modifier: Modifier = Modifier
    ) = DsLightItemImpl(
        modifier = modifier,
        color = Color.Yellow
    )

    @Composable
    fun WarningLight(
        modifier: Modifier = Modifier
    ) = DsLightItemImpl(
        modifier = modifier,
        color = Color.Red,
        isBlinking = true
    )
}

@Composable
private fun DsLightItemImpl(
    modifier: Modifier = Modifier,
    color: Color,
    isBlinking: Boolean = false
) {

    val visibleState = remember { mutableStateOf(true) }
    LaunchedEffect(isBlinking) {
        while (isBlinking) {
            delay(500)
            visibleState.value = !visibleState.value
        }
    }

    AnimatedVisibility(
        visible = visibleState.value,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Box(modifier = modifier.background(color = color, shape = CircleShape))
    }

}