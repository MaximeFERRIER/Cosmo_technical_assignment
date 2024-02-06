package fr.droidfactory.cosmo.sdk.bluetooth.controller

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import fr.droidfactory.cosmo.sdk.bluetooth.receivers.StateReceiver
import fr.droidfactory.cosmo.sdk.core.models.BluetoothDeviceFound
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class BluetoothControllerImpl @Inject constructor(
    private val context: Context
): BluetoothController {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager.adapter
    private val _isBluetoothEnabled = MutableStateFlow(false)
    override val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled.asStateFlow()
    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceFound>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceFound>> = _scannedDevices.asStateFlow()
    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceFound>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceFound>> = _pairedDevices.asStateFlow()

    private val stateReceiver = StateReceiver { isBluetoothEnable ->
        _isBluetoothEnabled.update { isBluetoothEnable }
    }


    override fun enableBluetooth() {
        if(!_isBluetoothEnabled.value) return
        if(context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) return
        context.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }

    override fun startDiscovery() {
        TODO("Not yet implemented")
    }

    override fun stopDiscovery() {
        TODO("Not yet implemented")
    }

    override fun release() {
        //context.unregisterReceiver(stateReceiver)
    }
}