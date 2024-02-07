package fr.droidfactory.cosmo.ui.products.discover

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.droidfactory.cosmo.sdk.core.ui.LocalWindowSizeProvider
import fr.droidfactory.cosmo.sdk.designsystem.components.DsBluetoothCard
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTopBar
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.discover.components.AskScreen
import fr.droidfactory.cosmo.ui.products.getPermissions
import fr.droidfactory.cosmo.ui.products.getTypeName

private typealias BluetoothDiscoverActioner = (BluetoothDiscoverActions) -> Unit

@Composable
internal fun BluetoothDiscoverStateful(
    viewModel: BluetoothDiscoverViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    val context = LocalContext.current
    val permissions = remember { getPermissions(context) }

    val bluetoothPermissionsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.entries.forEach {
                permissions.replace(it.key, it.value)
            }
        }
    val enableBluetoothLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    val state = viewModel.state.collectAsState()

    BluetoothDiscoverScreen(
        permissions = permissions,
        isBluetoothEnable = state.value.isBluetoothEnabled,
        isScanning = state.value.isScanning,
        discoveredDevices = state.value.scannedDevices
    ) { action ->
        when (action) {
            BluetoothDiscoverActions.OnAskForPermissionClicked -> bluetoothPermissionsLauncher.launch(
                permissions.filterValues { !it }.keys.toTypedArray()
            )

            BluetoothDiscoverActions.OnAskToTurnOnBluetoothClicked -> enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )

            BluetoothDiscoverActions.OnBackClicked -> {
                viewModel.stopDiscovery()
                onNavigationBack()
            }

            BluetoothDiscoverActions.OnToggleDevicesDiscovery -> {
                if (state.value.isScanning) {
                    viewModel.stopDiscovery()
                } else {
                    viewModel.startDiscovery()
                }
            }
        }
    }

}

@Composable
private fun BluetoothDiscoverScreen(
    permissions: MutableMap<String, Boolean>,
    isBluetoothEnable: Boolean,
    isScanning: Boolean,
    discoveredDevices: List<BluetoothDevice>,
    actioner: BluetoothDiscoverActioner
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DsTopBar.NavigationTopBar(title = stringResource(id = R.string.discovery_title)) {
                actioner(BluetoothDiscoverActions.OnBackClicked)
            }
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
        )
        when {
            permissions.any { !it.value } -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings),
                contentAlignment = Alignment.Center
            ) {
                AskScreen(
                    text = stringResource(id = R.string.ask_bluetooth_access),
                    textButton = stringResource(
                        id = R.string.ask_authorize
                    )
                ) {
                    actioner(BluetoothDiscoverActions.OnAskForPermissionClicked)
                }
            }

            !isBluetoothEnable -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings),
                contentAlignment = Alignment.Center
            ) {
                AskScreen(
                    text = stringResource(id = R.string.ask_bluetooth_activation),
                    textButton = stringResource(
                        id = R.string.ask_activation
                    )
                ) {
                    actioner(BluetoothDiscoverActions.OnAskToTurnOnBluetoothClicked)
                }
            }

            else -> DiscoveryScreen(
                paddings = paddings,
                isScanning = isScanning,
                discoveredDevices = discoveredDevices,
                actioner = actioner
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun DiscoveryScreen(
    paddings: PaddingValues,
    isScanning: Boolean,
    discoveredDevices: List<BluetoothDevice>,
    actioner: BluetoothDiscoverActioner
) {
    val nbColumns = LocalWindowSizeProvider.current.getNbColumns()
    if(!isScanning && discoveredDevices.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            IconButton(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary, shape = CircleShape),
                onClick = { actioner(BluetoothDiscoverActions.OnToggleDevicesDiscovery) }) {
                Icon(painter = painterResource(id = fr.droidfactory.cosmo.sdk.designsystem.R.drawable.bluetooth), contentDescription = "")
            }
        }
    } else {
        LazyVerticalGrid(modifier = Modifier.padding(paddings), columns = GridCells.Fixed(nbColumns)) {
            items(items = discoveredDevices, key = {
                "${it.address}_$nbColumns"
            }) {
                DsBluetoothCard(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), deviceName = it.name, address = it.address, it.type.getTypeName()) {

                }
            }
        }
    }
}

