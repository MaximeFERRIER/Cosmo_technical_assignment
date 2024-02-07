package fr.droidfactory.cosmo.sdk.bluetooth.controller

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val isBluetoothEnabled: StateFlow<Boolean>
    val isScanning: StateFlow<Boolean>

    fun registerBluetoothStateReceiver()
    fun startDiscovery()
    fun stopDiscovery()
    fun pairDevice(device: BluetoothDevice): Result<Unit>
    fun release()

}