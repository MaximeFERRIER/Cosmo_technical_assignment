package fr.droidfactory.cosmo.ui.products.discover.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.droidfactory.cosmo.sdk.designsystem.components.DsButton
import fr.droidfactory.cosmo.sdk.designsystem.components.DsLabelItem
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTexts
import fr.droidfactory.cosmo.ui.products.R
import fr.droidfactory.cosmo.ui.products.getIcon
import fr.droidfactory.cosmo.ui.products.getTypeName

@SuppressLint("MissingPermission")
@Composable
internal fun DeviceDetails(device: BluetoothDevice, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            DsButton.TextButton(text = stringResource(id = R.string.ok)) {
                onClose()
            }
        }, title = {
            DsTexts.HeadlineMedium(title = device.name)
        }, icon = {
            Icon(imageVector = device.bluetoothClass.majorDeviceClass.getIcon(), contentDescription = "")
        }, text = {
            Column {
                DsLabelItem.LabelText(
                    title = stringResource(id = R.string.label_type),
                    text = device.type.getTypeName(),
                    isFirstItemOfTheList = true,
                    isLastItemOfTheList = false
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                DsLabelItem.LabelText(
                    title = stringResource(id = R.string.label_mac_address),
                    text = device.address,
                    isFirstItemOfTheList = true,
                    isLastItemOfTheList = false
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                device.uuids.firstOrNull()?.let {
                    DsLabelItem.LabelText(
                        title = stringResource(id = R.string.label_uuid),
                        text = device.uuids.first().toString(),
                        isFirstItemOfTheList = true,
                        isLastItemOfTheList = false
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}