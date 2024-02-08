package fr.droidfactory.cosmo.ui.products.discover.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTexts

@Composable
internal fun Tuto(paddings: PaddingValues) {
    val animatedOffsetY = remember { mutableFloatStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "transition")
    val offsetAnimator = infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "transition_timer"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DsTexts.HeadlineLarge(title = "Start here")

        Spacer(modifier = Modifier.height(8.dp))


        Icon(
            modifier = Modifier
                .size(56.dp)
                .offset(y = animatedOffsetY.floatValue.dp),
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.tertiary
        )

    }

    LaunchedEffect(Unit) {
        snapshotFlow { offsetAnimator.value }
            .collect { value ->
                animatedOffsetY.floatValue = value
            }
    }
}