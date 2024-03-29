package fr.droidfactory.cosmo.ui.products.productlist

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.BluetoothSearching
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.LocalWindowSizeProvider
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.sdk.designsystem.components.DsFab
import fr.droidfactory.cosmo.sdk.designsystem.components.DsProductItem
import fr.droidfactory.cosmo.sdk.designsystem.components.DsSnackbars
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTopBar
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsErrorScreen
import fr.droidfactory.cosmo.sdk.designsystem.screens.DsLoadingScreen
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.getIllustration
import fr.droidfactory.cosmo.ui.products.toErrorMessage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private typealias ProductListActioner = (ProductListActions) -> Unit

@Composable
internal fun ProductListStateful(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigateToProductDetails: (String) -> Unit,
    onNavigateToBluetoothDiscovery: () -> Unit
) {
    val context = LocalContext.current
    val nbColumns = LocalWindowSizeProvider.current.getNbColumns()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val canScanBluetoothDevices =
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) && context.packageManager.hasSystemFeature(
            PackageManager.FEATURE_BLUETOOTH_LE
        )
    val state = viewModel.productsState.collectAsState()
    val sideEffect = remember {
        viewModel.sideEffect.flowWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
    }

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
            snackbarHostState = snackbarHostState,
            nbColumns = nbColumns,
            canScanBluetoothDevices = canScanBluetoothDevices
        ) { action ->
            when (action) {
                ProductListActions.OnFabClicked -> onNavigateToBluetoothDiscovery()
                is ProductListActions.OnProductClicked -> navigateToProductDetails(action.productMacAddress)
                ProductListActions.OnRefresh -> viewModel.getProductList()
            }
        }
    }

    LaunchedEffect(sideEffect) {
        sideEffect.onEach { error ->
            scope.launch {
                snackbarHostState.showSnackbar(message = error.toErrorMessage(context = context))
            }
        }.launchIn(this)
    }
}

@Composable
private fun ProductListScreen(
    products: List<Product>,
    nbColumns: Int,
    snackbarHostState: SnackbarHostState,
    canScanBluetoothDevices: Boolean,
    actioner: ProductListActioner
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            DsTopBar.NoNavigationTopBar(
                title = stringResource(id = R.string.product_title)
            )
        }, floatingActionButton = {
            if (canScanBluetoothDevices) {
                DsFab.SecondaryFab(
                    text = stringResource(id = R.string.discovery_title),
                    icon = Icons.AutoMirrored.Default.BluetoothSearching
                ) {
                    actioner(ProductListActions.OnFabClicked)
                }
            }
        }, snackbarHost = {
            SnackbarHost(snackbarHostState) {
                DsSnackbars.SnackbarError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    message = it.visuals.message
                )
            }
        }
    ) { paddings ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            columns = GridCells.Fixed(nbColumns)
        ) {
            items(items = products, key = { "${nbColumns}_${it.macAddress}" }) {
                DsProductItem(
                    title = it.productCompleteName,
                    picture = it.model.getIllustration()
                ) {
                    actioner(ProductListActions.OnProductClicked(it.macAddress))
                }
            }
        }
    }
}