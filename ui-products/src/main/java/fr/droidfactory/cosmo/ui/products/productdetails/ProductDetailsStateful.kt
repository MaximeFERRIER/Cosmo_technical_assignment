package fr.droidfactory.cosmo.ui.products.productdetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsErrorScreen
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsLoadingScreen
import fr.droidfactory.cosmo.ui.products.R

@Composable
internal fun ProductDetailsStateful(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    
    val state = viewModel.product.collectAsState()
    
    when(state.value) {
        is ResultState.Failure -> DsErrorScreen(title = stringResource(id = R.string.error_title), subTitle = stringResource(id = R.string.error_product_not_found), onCloseClicked = { onNavigationBack( )})
        ResultState.Uninitialized, ResultState.Loading -> DsLoadingScreen(loadingText = stringResource(id = R.string.loading_product_details))
        is ResultState.Success -> ProductDetailsScreen(
            product = (state.value as ResultState.Success<Product>).data
        )
    }
    
}

@Composable
private fun ProductDetailsScreen(product: Product) {
    Text(text = product.macAddress)
}