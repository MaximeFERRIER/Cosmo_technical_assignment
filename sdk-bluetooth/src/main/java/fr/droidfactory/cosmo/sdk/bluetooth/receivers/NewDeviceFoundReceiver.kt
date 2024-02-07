package fr.droidfactory.cosmo.sdk.bluetooth.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

internal class NewDeviceFoundReceiver (
    private val onNewDeviceFound: (BluetoothDevice) -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == BluetoothDevice.ACTION_FOUND) {
            val bluetoothDevice = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    BluetoothDevice.EXTRA_DEVICE,
                    BluetoothDevice::class.java
                )
            } else {
                intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            }

            bluetoothDevice?.let {
                onNewDeviceFound(it)
            }
        }
    }
}