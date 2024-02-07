package fr.droidfactory.cosmo.sdk.bluetooth.controller

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import fr.droidfactory.cosmo.sdk.bluetooth.receivers.NewDeviceFoundReceiver
import fr.droidfactory.cosmo.sdk.bluetooth.receivers.StateReceiver
import fr.droidfactory.cosmo.sdk.core.models.CosmoExceptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class BluetoothControllerImpl @Inject constructor(
    private val context: Context
) : BluetoothController {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager.adapter
    private val _isBluetoothEnabled = MutableStateFlow(bluetoothAdapter.isEnabled)
    override val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled.asStateFlow()
    private val _scannedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDevice>> = _scannedDevices.asStateFlow()
    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDevice>> = _pairedDevices.asStateFlow()
    private val _isScanning = MutableStateFlow(false)
    override val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val stateReceiver = StateReceiver { isBluetoothEnable ->
        _isBluetoothEnabled.update { isBluetoothEnable }
    }

    private val newDeviceFoundReceiver = NewDeviceFoundReceiver { device ->
        _scannedDevices.update { devices ->
            if (device in devices) devices else devices + device
        }
    }

    override fun registerBluetoothStateReceiver() {
        context.registerReceiver(stateReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    @SuppressLint("MissingPermission")
    override fun startDiscovery() {
        if (!hasPermissions() || bluetoothAdapter?.isDiscovering == true) return
        context.registerReceiver(newDeviceFoundReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        bluetoothAdapter?.startDiscovery()
        _isScanning.update { true }
        _pairedDevices.update {
            bluetoothAdapter?.bondedDevices?.map { it } ?: emptyList()
        }
    }

    @SuppressLint("MissingPermission")
    override fun stopDiscovery() {
        if (!hasPermissions()) return
        bluetoothAdapter?.cancelDiscovery()
        _isScanning.update { false }
    }

    private fun hasPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }

        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_ADMIN
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    @SuppressLint("MissingPermission")
    override fun pairDevice(device: BluetoothDevice): Result<Unit> {
        if (!hasPermissions()) return Result.failure(CosmoExceptions.MissingPermissionsException)

        stopDiscovery()
        val newDevice = bluetoothAdapter.getRemoteDevice(device.address)

        if (newDevice.bondState == BluetoothDevice.BOND_NONE) {
            if (newDevice.createBond()) {
                _pairedDevices.update { devices ->
                    if(newDevice in devices) devices else devices + newDevice
                }

                _scannedDevices.update {
                    it - newDevice
                }
                return Result.success(Unit)
            }
            return Result.failure(CosmoExceptions.FailedPairDeviceException)
        }
        return Result.failure(CosmoExceptions.AlreadydPairDeviceException)
    }

    override fun release() {
        try {
            stopDiscovery()
            context.unregisterReceiver(stateReceiver)
            context.unregisterReceiver(newDeviceFoundReceiver)
        } catch (_: Exception) {
        }
    }
}