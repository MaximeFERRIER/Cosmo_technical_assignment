package fr.droidfactory.cosmo.sdk.bluetooth.receivers

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class StateReceiver(
    private val isBluetoothEnable: (Boolean) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            isBluetoothEnable(
                intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                ) == BluetoothAdapter.STATE_ON
            )
        }
    }
}