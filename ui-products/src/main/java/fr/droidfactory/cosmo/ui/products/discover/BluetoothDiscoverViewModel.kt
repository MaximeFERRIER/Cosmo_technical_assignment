package fr.droidfactory.cosmo.ui.products.discover

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.droidfactory.cosmo.sdk.bluetooth.controller.BluetoothController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BluetoothDiscoverViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) : ViewModel() {

    internal val state = combine(
        bluetoothController.isBluetoothEnabled,
        bluetoothController.scannedDevices,
        bluetoothController.isScanning
    ) { isBluetoothEnabled, scannedDevices, isScanning ->
        BluetoothDiscoverDataStore(
            isBluetoothEnabled = isBluetoothEnabled,
            isScanning = isScanning,
            scannedDevices = scannedDevices
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, BluetoothDiscoverDataStore())

    init {
        launchObservers()
    }

    private fun launchObservers() {
        bluetoothController.registerBluetoothStateReceiver()
    }

    internal fun startDiscovery() {
        bluetoothController.startDiscovery()
    }

    internal fun stopDiscovery() {
        bluetoothController.stopDiscovery()
    }


    private fun killObservers() {
        viewModelScope.launch {
            bluetoothController.stopDiscovery()
            bluetoothController.release()
        }
    }

    override fun onCleared() {
        killObservers()
        super.onCleared()
    }
}

internal data class BluetoothDiscoverDataStore(
    val isBluetoothEnabled: Boolean = false,
    val isScanning: Boolean = false,
    val scannedDevices: List<BluetoothDevice> = emptyList()
)