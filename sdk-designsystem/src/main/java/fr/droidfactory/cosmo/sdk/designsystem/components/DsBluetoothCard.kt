package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.droidfactory.cosmo.sdk.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsBluetoothCard(
    modifier: Modifier = Modifier,
    deviceName: String?,
    address: String,
    typeName: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onClick() }
    ) {
        DsTexts.HeadlineSmall(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), title = deviceName ?: stringResource(id = R.string.no_name), align = TextAlign.Center
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        DsTexts.BodyMedium(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), title = address)

        DsTexts.BodyMedium(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), title = typeName)
    }
}