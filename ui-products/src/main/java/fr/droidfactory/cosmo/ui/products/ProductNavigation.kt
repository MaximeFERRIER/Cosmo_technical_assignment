package fr.droidfactory.cosmo.ui.products

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.droidfactory.cosmo.sdk.designsystem.navigation.setComposable
import fr.droidfactory.cosmo.ui.products.productdetails.ProductDetailsStateful
import fr.droidfactory.cosmo.ui.products.productlist.ProductListStateful

@Composable
fun ProductNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.ProductsList.route) {
        setComposable(route = Screens.ProductsList.route) {
            ProductListStateful(
                navigateToProductDetails = {
                    navController.navigate(Screens.ProductDetails.buildRoute(macaddress = it))
                }
            )
        }

        setComposable(route = Screens.ProductDetails.routeWithArguments,
            arguments = listOf(navArgument(Screens.ProductDetails.argumentMacAddress) { type = NavType.StringType })
        ) {
            ProductDetailsStateful(onNavigationBack = {
                navController.popBackStack()
            })
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