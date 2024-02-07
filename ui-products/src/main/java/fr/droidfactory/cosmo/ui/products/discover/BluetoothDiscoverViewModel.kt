package fr.droidfactory.cosmo.ui.products.discover

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.droidfactory.cosmo.sdk.bluetooth.controller.BluetoothController
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BluetoothDiscoverViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) : ViewModel() {

    private var observeBluetoothEnabledJob: Job? = null
    private var observeScanDiscoveryJob: Job? = null
    private val _isBluetoothEnabled = MutableStateFlow(false)
    private val _scannedDevices = MutableStateFlow(emptyList<BluetoothDevice>())

    internal val state = combine(
        _isBluetoothEnabled,
        _scannedDevices,
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
        observeBluetoothState()
        observeScannedDevices()
    }

    private fun observeBluetoothState() {
        observeBluetoothEnabledJob?.cancel()
        observeBluetoothEnabledJob = viewModelScope.launch {
            bluetoothController.isBluetoothEnabled.collect { isEnable ->
                _isBluetoothEnabled.update { isEnable }
            }
        }
    }

    private fun observeScannedDevices() {
        observeScanDiscoveryJob?.cancel()
        observeScanDiscoveryJob = viewModelScope.launch {
            bluetoothController.scannedDevices.collect { devices ->
                _scannedDevices.update { devices }
            }
        }
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
            observeBluetoothEnabledJob?.cancelAndJoin()
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