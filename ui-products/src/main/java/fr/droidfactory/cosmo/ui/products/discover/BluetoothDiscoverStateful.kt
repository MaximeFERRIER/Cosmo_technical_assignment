package fr.droidfactory.cosmo.ui.products.discover

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTopBar
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.discover.components.AskScreen

private typealias BluetoothDiscoverActioner = (BluetoothDiscoverActions) -> Unit

@Composable
internal fun BluetoothDiscoverStateful(
    viewModel: BluetoothDiscoverViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    val context = LocalContext.current
    val permissions = remember {
        mutableStateMapOf<String, Boolean>()
    }
    permissions[Manifest.permission.BLUETOOTH_CONNECT] =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    permissions[context.getString(R.string.bluetooth_permission)] =
        context.checkSelfPermission(context.getString(R.string.bluetooth_permission)) == PackageManager.PERMISSION_GRANTED
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
        isBluetoothEnable = state.value.isBluetoothEnabled
    ) { action ->
        when (action) {
            BluetoothDiscoverActions.OnAskForPermissionClicked -> bluetoothPermissionsLauncher.launch(
                permissions.filterValues { !it }.keys.toTypedArray()
            )

            BluetoothDiscoverActions.OnAskToTurnOnBluetoothClicked -> enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )

            BluetoothDiscoverActions.OnBackClicked -> onNavigationBack()
        }
    }

    LifecycleStartEffect {
        viewModel.launchObservers()
        onStopOrDispose { viewModel.killObservers() }
    }

}

@Composable
private fun BluetoothDiscoverScreen(
    permissions: MutableMap<String, Boolean>,
    isBluetoothEnable: Boolean,
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
                AskScreen(text = "Accorder les permissions", textButton = "Enable") {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddings), text = "Activer bluetooth", textButton = "Activation"
                ) {
                    actioner(BluetoothDiscoverActions.OnAskToTurnOnBluetoothClicked)
                }
            }

            else -> {}
        }
    }
}