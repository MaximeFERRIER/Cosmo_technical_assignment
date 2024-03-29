package fr.droidfactory.cosmo.ui.products.discover

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.flowWithLifecycle
import fr.droidfactory.cosmo.sdk.core.models.CosmoExceptions
import fr.droidfactory.cosmo.sdk.core.ui.LocalWindowSizeProvider
import fr.droidfactory.cosmo.sdk.designsystem.components.DsBluetoothCard
import fr.droidfactory.cosmo.sdk.designsystem.components.DsButton
import fr.droidfactory.cosmo.sdk.designsystem.components.DsSnackbars
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTexts
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTopBar
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.discover.components.AskScreen
import fr.droidfactory.cosmo.ui.products.discover.components.DeviceDetails
import fr.droidfactory.cosmo.ui.products.discover.components.Tuto
import fr.droidfactory.cosmo.ui.products.getIcon
import fr.droidfactory.cosmo.ui.products.getPermissions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private typealias BluetoothDiscoverActioner = (BluetoothDiscoverActions) -> Unit

@Composable
internal fun BluetoothDiscoverStateful(
    viewModel: BluetoothDiscoverViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val successSnackbarHostState = remember { SnackbarHostState() }
    val permissions = remember { getPermissions(context) }
    val arePermissionsGranted = remember {
        mutableStateOf(permissions.all { it.value })
    }
    val displayDeviceData = remember { mutableStateOf<BluetoothDevice?>(null) }

    val bluetoothPermissionsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.entries.forEach {
                permissions.replace(it.key, it.value)
            }
            arePermissionsGranted.value = permissions.all { it.value }
        }
    val enableBluetoothLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    val state = viewModel.state.collectAsState()
    val sideEffect = remember {
        viewModel.sideEffect.flowWithLifecycle(lifecycle.lifecycle, Lifecycle.State.RESUMED)
    }

    BluetoothDiscoverScreen(
        arePermissionsGranted = arePermissionsGranted.value,
        isBluetoothEnable = state.value.isBluetoothEnabled,
        isScanning = state.value.isScanning,
        discoveredDevices = state.value.scannedDevices,
        pairedDevices = state.value.pairedDevices,
        pairingDevice = state.value.pairingDevice,
        errorSnackbarHostState = errorSnackbarHostState,
        successSnackbarHostState = successSnackbarHostState,
        displayDeviceData = displayDeviceData
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

            is BluetoothDiscoverActions.OnDeviceClicked -> {
                if (action.device in state.value.pairedDevices) {
                    displayDeviceData.value = action.device
                } else {
                    viewModel.pairDevice(action.device)
                }
            }

            BluetoothDiscoverActions.OnCloseDialog -> displayDeviceData.value = null
        }
    }

    LaunchedEffect(key1 = sideEffect) {
        sideEffect.onEach { error ->
            val snackbarText = when (error) {
                null -> R.string.success_pair_device
                is CosmoExceptions.MissingPermissionsException -> R.string.error_missing_permission
                is CosmoExceptions.FailedPairDeviceException -> R.string.error_failed_pair_device
                is CosmoExceptions.AlreadydPairDeviceException -> R.string.error_device_already_paired
                else -> R.string.error_title
            }
            scope.launch {
                if (error == null) {
                    successSnackbarHostState.showSnackbar(message = context.getString(snackbarText))
                } else {
                    errorSnackbarHostState.showSnackbar(message = context.getString(snackbarText))
                }
            }
        }.launchIn(this)
    }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.stopDiscovery()
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            viewModel.stopDiscovery()
            lifecycle.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun BluetoothDiscoverScreen(
    arePermissionsGranted: Boolean,
    isBluetoothEnable: Boolean,
    isScanning: Boolean,
    discoveredDevices: List<BluetoothDevice>,
    pairedDevices: List<BluetoothDevice>,
    pairingDevice: BluetoothDevice?,
    errorSnackbarHostState: SnackbarHostState,
    successSnackbarHostState: SnackbarHostState,
    displayDeviceData: MutableState<BluetoothDevice?>,
    actioner: BluetoothDiscoverActioner,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            DsTopBar.NavigationTopBar(title = stringResource(id = R.string.discovery_title)) {
                actioner(BluetoothDiscoverActions.OnBackClicked)
            }
        }, bottomBar = {
            if (arePermissionsGranted && isBluetoothEnable) {
                val buttonText = if (isScanning) {
                    R.string.stop_scanning
                } else {
                    R.string.start_scanning
                }
                DsButton.PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), text = stringResource(id = buttonText)
                ) {
                    actioner(BluetoothDiscoverActions.OnToggleDevicesDiscovery)
                }
            }
        }, snackbarHost = {
            SnackbarHost(hostState = errorSnackbarHostState) {
                DsSnackbars.SnackbarError(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    message = it.visuals.message
                )
            }

            SnackbarHost(hostState = successSnackbarHostState) {
                DsSnackbars.SnackbarSuccess(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    message = it.visuals.message
                )
            }
        }
    ) { paddings ->
        when {
            !arePermissionsGranted -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings),
                contentAlignment = Alignment.Center
            ) {
                AskScreen(
                    modifier = Modifier.padding(16.dp),
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
                    modifier = Modifier.padding(16.dp),
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
                pairedDevices = pairedDevices,
                pairingDevice = pairingDevice,
                actioner = actioner
            )
        }

        displayDeviceData.value?.let {
            DeviceDetails(
                device = it,
                onClose = {
                    actioner(BluetoothDiscoverActions.OnCloseDialog)
                }
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
    pairedDevices: List<BluetoothDevice>,
    pairingDevice: BluetoothDevice?,
    actioner: BluetoothDiscoverActioner
) {
    if (!isScanning && discoveredDevices.isEmpty()) {
        Tuto(paddings = paddings)
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddings)
        ) {
            var tabIndex by rememberSaveable { mutableIntStateOf(0) }
            TabRow(modifier = Modifier, selectedTabIndex = tabIndex) {
                listOf(
                    stringResource(id = R.string.tab_discover),
                    stringResource(id = R.string.tab_bounded)
                ).forEachIndexed { index, tabName ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { DsTexts.BodyMedium(title = tabName) })
                }
            }
            when (tabIndex) {
                0 -> DeviceList(
                    devices = discoveredDevices,
                    pairingDevice = pairingDevice,
                    actioner = actioner
                )

                1 -> DeviceList(
                    devices = pairedDevices,
                    pairingDevice = null,
                    actioner = actioner
                )
            }
        }

    }
}

@SuppressLint("MissingPermission")
@Composable
private fun DeviceList(
    devices: List<BluetoothDevice>,
    pairingDevice: BluetoothDevice?,
    actioner: BluetoothDiscoverActioner
) {
    val nbColumns = LocalWindowSizeProvider.current.getNbColumns() * 2
    LazyVerticalGrid(modifier = Modifier.fillMaxWidth(), columns = GridCells.Fixed(nbColumns)) {
        items(items = devices, key = {
            "${it.address}_$nbColumns"
        }) {
            DsBluetoothCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                isPairing = it.address == pairingDevice?.address,
                deviceName = it.name,
                deviceType = it.bluetoothClass.majorDeviceClass.getIcon()
            ) {
                actioner(BluetoothDiscoverActions.OnDeviceClicked(it))
            }
        }
    }
}

