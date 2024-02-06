package fr.droidfactory.cosmo.ui.products.productlist

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.LocalWindowSizeProvider
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.sdk.core.ui.WindowSizeProvider
import fr.droidfactory.cosmo.sdk.designsystem.components.DsProductItem
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTopBar
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsErrorScreen
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsLoadingScreen
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.getIllustration

private typealias ProductListActioner = (ProductListActions) -> Unit

@Composable
internal fun ProductListStateful(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigateToProductDetails: (String) -> Unit
) {
    val screenSize = LocalWindowSizeProvider.current.getScreenSize()
    val state = viewModel.productsState.collectAsState()

    when (state.value) {
        ResultState.Uninitialized, ResultState.Loading -> DsLoadingScreen(
            loadingText = stringResource(id = R.string.loading_products_text)
        )

        is ResultState.Failure -> DsErrorScreen(
            title = stringResource(id = R.string.error_title),
            subTitle = (state.value as ResultState.Failure).exception.toString(),
            buttonText = stringResource(id = R.string.retry_label),
            onButtonClick = { viewModel.getProductList() }
        )

        is ResultState.Success -> ProductListScreen(
            products = (state.value as ResultState.Success<List<Product>>).data,
            screenSize = screenSize
        ) { action ->
            when (action) {
                ProductListActions.OnFabClicked -> TODO()
                is ProductListActions.OnProductClicked -> navigateToProductDetails(action.productMacAddress)
                ProductListActions.OnRefresh -> viewModel.getProductList()
            }
        }
    }


}

@Composable
private fun ProductListScreen(
    products: List<Product>,
    screenSize: WindowSizeProvider.ScreenSize,
    actioner: ProductListActioner
) {

    val nbColumns = when (screenSize) {
        WindowSizeProvider.ScreenSize.SMALL -> 1
        WindowSizeProvider.ScreenSize.MEDIUM -> 2
        WindowSizeProvider.ScreenSize.LARGE -> 4
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            DsTopBar.NoNavigationTopBar(
                title = stringResource(id = R.string.product_title)
            )
        }, floatingActionButton = {

        }, snackbarHost = {

        }
    ) { paddings ->

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            columns = GridCells.Fixed(nbColumns)
        ) {
            items(items = products, key = { "${nbColumns}_${it.macAddress}"}) {
                DsProductItem(title = it.productCompleteName, picture = it.model.getIllustration()) {

                }
            }
        }

    }
}