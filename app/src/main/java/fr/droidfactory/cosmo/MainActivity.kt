package fr.droidfactory.cosmo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import fr.droidfactory.cosmo.sdk.core.ui.LocalWindowSizeProvider
import fr.droidfactory.cosmo.sdk.core.ui.WindowSizeProvider
import fr.droidfactory.cosmo.sdk.designsystem.theme.CosmoTheme
import fr.droidfactory.cosmo.ui.products.ProductNavigation

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            CompositionLocalProvider(
                LocalWindowSizeProvider provides WindowSizeProvider(windowSizeClass)
            ) {
                CosmoTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ProductNavigation()
                    }
                }
            }

        }
    }
}

