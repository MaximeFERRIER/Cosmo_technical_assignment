package fr.droidfactory.cosmo.ui.products

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.droidfactory.cosmo.sdk.designsystem.navigation.setComposable

@Composable
fun ProductNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.ProductsList.route) {
        setComposable(route = Screens.ProductsList.route) {

        }
    }
}

internal sealed class Screens(val route: String) {
    data object ProductsList : Screens("route_product_list")
    data object ProductDetails: Screens("route_product_details") {
        const val argumentMacAddress = "macaddress"
        val routeWithArguments = "$route?$argumentMacAddress={$argumentMacAddress}"
        fun buildRoute(macaddress: String) = "$route?$argumentMacAddress=$macaddress"
    }
}