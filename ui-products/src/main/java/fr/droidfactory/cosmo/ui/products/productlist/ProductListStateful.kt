package fr.droidfactory.cosmo.ui.products.productlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsLoadingScreen
import fr.droidfactory.cosmo.ui.products.R

private typealias ProductListActioner = (ProductListActions) -> Unit
@Composable
internal fun ProductListStateful(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigateToProductDetails: (String) -> Unit
) {

    val state = viewModel.productsState.collectAsState()

    ProductListScreen(
        productState = state.value
    ) { action ->
        when(action) {
            ProductListActions.OnFabClicked -> TODO()
            is ProductListActions.OnProductClicked -> navigateToProductDetails(action.productMacAddress)
            ProductListActions.OnRefresh -> TODO()
        }
    }
}

@Composable
private fun ProductListScreen(
    productState: ResultState<List<Product>>,
    actioner: ProductListActioner
) {
    when(productState) {
        ResultState.Uninitialized, ResultState.Loading -> DsLoadingScreen(loadingText = stringResource(id = R.string.loading_products_text))
        is ResultState.Failure -> TODO()
        is ResultState.Success -> TODO()
    }
}