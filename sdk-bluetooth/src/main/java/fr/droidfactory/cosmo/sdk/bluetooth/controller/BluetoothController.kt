package fr.droidfactory.cosmo.sdk.bluetooth.controller

import fr.droidfactory.cosmo.sdk.core.models.BluetoothDeviceFound
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<BluetoothDeviceFound>>
    val pairedDevices: StateFlow<List<BluetoothDeviceFound>>
    val isBluetoothEnabled: StateFlow<Boolean>

    fun enableBluetooth()
    fun startDiscovery()
    fun stopDiscovery()
    fun release()

}