package fr.droidfactory.cosmo.ui.products.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.droidfactory.cosmo.sdk.bluetooth.controller.BluetoothController
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BluetoothDiscoverViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) : ViewModel() {

    private var observeBluetoothEnabledJob: Job? = null
    private var _isBluetoothEnabled = MutableStateFlow(false)
    internal val isBluetoothEnabled = _isBluetoothEnabled


    internal fun launchObservers() {
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

    internal fun killObservers() {
        viewModelScope.launch {
            bluetoothController.release()
            observeBluetoothEnabledJob?.cancelAndJoin()
        }

    }
}