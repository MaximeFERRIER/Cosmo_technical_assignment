package fr.droidfactory.cosmo.ui.products.productlist

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

private typealias ProductListActioner = (ProductListActions) -> Unit
@Composable
internal fun ProductListStateful(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigateToProductDetails: (String) -> Unit
) {
    ProductListScreen() { action ->
        when(action) {
            ProductListActions.OnFabClicked -> TODO()
            is ProductListActions.OnProductClicked -> navigateToProductDetails(action.productMacAddress)
            ProductListActions.OnRefresh -> TODO()
        }
    }
}

@Composable
private fun ProductListScreen(
    actioner: ProductListActioner
) {

}