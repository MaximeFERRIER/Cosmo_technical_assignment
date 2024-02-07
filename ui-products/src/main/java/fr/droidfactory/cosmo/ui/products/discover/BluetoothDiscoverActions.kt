package fr.droidfactory.cosmo.ui.products.discover

import android.bluetooth.BluetoothDevice

internal sealed interface BluetoothDiscoverActions {
    data object OnBackClicked: BluetoothDiscoverActions
    data object OnAskForPermissionClicked: BluetoothDiscoverActions
    data object OnAskToTurnOnBluetoothClicked: BluetoothDiscoverActions
    data object OnToggleDevicesDiscovery: BluetoothDiscoverActions
    data class OnDeviceClicked(val device: BluetoothDevice): BluetoothDiscoverActions
    data object OnCloseDialog: BluetoothDiscoverActions
}