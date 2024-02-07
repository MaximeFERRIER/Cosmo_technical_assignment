package fr.droidfactory.cosmo.ui.products.discover

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import fr.droidfactory.cosmo.sdk.core.models.CosmoExceptions
import fr.droidfactory.cosmo.sdk.core.ui.LocalWindowSizeProvider
import fr.droidfactory.cosmo.sdk.designsystem.components.DsBluetoothCard
import fr.droidfactory.cosmo.sdk.designsystem.components.DsButton
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTexts
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTopBar
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.discover.components.AskScreen
import fr.droidfactory.cosmo.ui.products.getIcon
import fr.droidfactory.cosmo.ui.products.getPermissions
import fr.droidfactory.cosmo.ui.products.getTypeName
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private typealias BluetoothDiscoverActioner = (BluetoothDiscoverActions) -> Unit

@Composable
internal fun BluetoothDiscoverStateful(
    viewModel: BluetoothDiscoverViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val permissions = remember { getPermissions(context) }
    val arePermissionsGranted = remember {
        mutableStateOf(permissions.all { it.value })
    }

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
        viewModel.sideEffect.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
    }

    BluetoothDiscoverScreen(
        arePermissionsGranted = arePermissionsGranted.value,
        isBluetoothEnable = state.value.isBluetoothEnabled,
        isScanning = state.value.isScanning,
        discoveredDevices = state.value.scannedDevices,
        pairedDevices = state.value.pairedDevices,
        pairingDevice = state.value.pairingDevice
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

            is BluetoothDiscoverActions.OnBoundDevice -> {
                viewModel.pairDevice(action.device)
            }
        }
    }

    LaunchedEffect(key1 = sideEffect) {
        sideEffect.onEach {
            when (it) {
                null -> {}
                is CosmoExceptions.MissingPermissionsException -> {}
                is CosmoExceptions.FailedPairDeviceException -> {}
            }
        }.launchIn(this)
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
    actioner: BluetoothDiscoverActioner
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DsTopBar.NavigationTopBar(title = stringResource(id = R.string.discovery_title)) {
                actioner(BluetoothDiscoverActions.OnBackClicked)
            }
        }, bottomBar = {
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
        val animatedOffsetY = remember { mutableFloatStateOf(0f) }
        val infiniteTransition = rememberInfiniteTransition(label = "transition")
        val offsetAnimator = infiniteTransition.animateFloat(
            initialValue = -15f,
            targetValue = 15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "transition_timer"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DsTexts.HeadlineLarge(title = "Start here")

            Spacer(modifier = Modifier.height(8.dp))


            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .offset(y = animatedOffsetY.floatValue.dp),
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )

        }

        LaunchedEffect(Unit) {
            snapshotFlow { offsetAnimator.value }
                .collect { value ->
                    animatedOffsetY.floatValue = value
                }
        }

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
    val nbColumns = LocalWindowSizeProvider.current.getNbColumns()
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
                address = it.address,
                typeName = it.type.getTypeName(),
                deviceType = it.bluetoothClass.majorDeviceClass.getIcon()
            ) {
                actioner(BluetoothDiscoverActions.OnBoundDevice(it))
            }
        }
    }
}

