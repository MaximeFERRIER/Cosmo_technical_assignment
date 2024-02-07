package fr.droidfactory.cosmo.ui.products.discover

internal sealed interface BluetoothDiscoverActions {
    data object OnBackClicked: BluetoothDiscoverActions
    data object OnAskForPermissionClicked: BluetoothDiscoverActions
    data object OnAskToTurnOnBluetoothClicked: BluetoothDiscoverActions
    data object OnToggleDevicesDiscovery: BluetoothDiscoverActions
}