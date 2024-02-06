package fr.droidfactory.cosmo.ui.products.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.droidfactory.cosmo.sdk.bluetooth.controller.BluetoothController
import fr.droidfactory.cosmo.sdk.core.models.BluetoothDeviceFound
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
    private val _isBluetoothEnabled = MutableStateFlow(false)
    private val _scannedDevices = MutableStateFlow(emptyList<BluetoothDeviceFound>())

    internal val state = combine(
        _isBluetoothEnabled,
        _scannedDevices
    ) { isBluetoothEnabled, scannedDevices ->
        BluetoothDiscoverDataStore(
            isBluetoothEnabled = isBluetoothEnabled,
            scannedDevices = scannedDevices
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, BluetoothDiscoverDataStore())

    init {
        launchObservers()
    }

    private fun launchObservers() {
        bluetoothController.registerReceivers()
        observeBluetoothState()
    }

    private fun observeBluetoothState() {
        observeBluetoothEnabledJob?.cancel()
        observeBluetoothEnabledJob = viewModelScope.launch {
            bluetoothController.isBluetoothEnabled.collect { isEnable ->
                _isBluetoothEnabled.update { isEnable }
            }
        }
    }


    private fun killObservers() {
        viewModelScope.launch {
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
    val scannedDevices: List<BluetoothDeviceFound> = emptyList()
)