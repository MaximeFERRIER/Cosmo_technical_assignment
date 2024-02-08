package fr.droidfactory.cosmo.sdk.bluetooth.receivers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

internal class NewDevicePairedReceiver (
    private val onNewDevicePaired: (BluetoothDevice) -> Unit
) : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
            val bluetoothDevice = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    BluetoothDevice.EXTRA_DEVICE,
                    BluetoothDevice::class.java
                )
            } else {
                intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            }

            bluetoothDevice?.let {
                if(it.bondState == BluetoothDevice.BOND_BONDED) {
                    onNewDevicePaired(it)
                }
            }
        }
    }
}