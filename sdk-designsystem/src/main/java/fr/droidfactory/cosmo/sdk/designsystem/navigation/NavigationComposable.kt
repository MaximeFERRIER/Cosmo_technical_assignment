package fr.droidfactory.cosmo.sdk.designsystem.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.setComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    composableScreen: @Composable (NavBackStackEntry) -> Unit
) {
    this.composable(
        route = route,
        enterTransition = {
            slideInVertically(animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutVertically(animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
        },
        arguments = arguments,
        deepLinks = deepLinks
    ) {
        composableScreen(it)
    }
}