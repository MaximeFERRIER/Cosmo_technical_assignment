package fr.droidfactory.cosmo.ui.products.discover

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.droidfactory.cosmo.sdk.bluetooth.controller.BluetoothController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BluetoothDiscoverViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) : ViewModel() {

    private val _pairingDeviceState = MutableStateFlow<BluetoothDevice?>(null)
    private val _sideEffect = Channel<Throwable?>(Channel.BUFFERED)
    internal val sideEffect = _sideEffect.receiveAsFlow()

    internal val state = combine(
        bluetoothController.isBluetoothEnabled,
        bluetoothController.scannedDevices,
        bluetoothController.isScanning,
        bluetoothController.pairedDevices,
        _pairingDeviceState
    ) { isBluetoothEnabled, scannedDevices, isScanning, pairedDevices, pairingDeviceState ->
        BluetoothDiscoverDataStore(
            isBluetoothEnabled = isBluetoothEnabled,
            isScanning = isScanning,
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            pairingDevice = pairingDeviceState
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, BluetoothDiscoverDataStore())

    init {
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

    internal fun pairDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            bluetoothController.pairDevice(device)
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
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val pairingDevice: BluetoothDevice? = null
)