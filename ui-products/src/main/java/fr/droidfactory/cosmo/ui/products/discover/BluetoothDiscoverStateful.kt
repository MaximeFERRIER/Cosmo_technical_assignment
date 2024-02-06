package fr.droidfactory.cosmo.ui.products.discover

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect

private typealias BluetoothDiscoverActioner = (BluetoothDiscoverActions) -> Unit
@Composable
internal fun BluetoothDiscoverStateful(
    viewModel: BluetoothDiscoverViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {

    val state = viewModel.isBluetoothEnabled.collectAsState()

    BluetoothDiscoverScreen(
        isBluetoothEnabled = state.value
    ) { action ->
         when(action) {
             BluetoothDiscoverActions.OnAskForPermissionClicked -> TODO()
             BluetoothDiscoverActions.OnAskToTurnOnBluetoothClicked -> TODO()
             BluetoothDiscoverActions.OnBackClicked -> onNavigationBack()
         }
    }

    LifecycleResumeEffect {
        viewModel.launchObservers()
        onPauseOrDispose { viewModel.killObservers() }
    }
}

@Composable
private fun BluetoothDiscoverScreen(
    isBluetoothEnabled: Boolean,
    actioner: BluetoothDiscoverActioner
) {
    Text(text = "$isBluetoothEnabled")
}