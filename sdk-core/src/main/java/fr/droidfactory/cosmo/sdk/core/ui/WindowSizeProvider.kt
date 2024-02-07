package fr.droidfactory.cosmo.sdk.core.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf


val LocalWindowSizeProvider: ProvidableCompositionLocal<WindowSizeProvider> =
    staticCompositionLocalOf {
        error("LocalWindowSizeProvider not provided")
    }

class WindowSizeProvider(private val windowSize: WindowSizeClass) {
    enum class ScreenSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    fun getScreenSize(): ScreenSize = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> ScreenSize.SMALL
        WindowWidthSizeClass.Medium -> ScreenSize.MEDIUM
        WindowWidthSizeClass.Expanded -> ScreenSize.LARGE
        else -> ScreenSize.SMALL
    }

    fun getNbColumns(): Int = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 4
        else -> 1
    }
}

@Composable
fun ProvideWindowSizeProvider(windowSize: WindowSizeClass, content: @Composable () -> Unit) {
    val provider = remember { WindowSizeProvider(windowSize) }
    CompositionLocalProvider(LocalWindowSizeProvider provides provider) {
        content()
    }
}