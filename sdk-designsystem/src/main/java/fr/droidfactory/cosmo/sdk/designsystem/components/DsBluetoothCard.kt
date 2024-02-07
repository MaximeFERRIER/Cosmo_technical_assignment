package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.droidfactory.cosmo.sdk.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsBluetoothCard(
    modifier: Modifier = Modifier,
    isPairing: Boolean,
    deviceName: String?,
    address: String,
    typeName: String,
    deviceType: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth().aspectRatio(1f),
        onClick = { onClick() },
        enabled = !isPairing
    ) {

        Icon(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
            imageVector = deviceType,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        DsTexts.TitleMedium(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            title = deviceName ?: stringResource(id = R.string.no_name),
            align = TextAlign.Center,
            maxLines = 2
        )

       /* DsTexts.BodyMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), title = address
        )

        DsTexts.BodyMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), title = typeName
        )*/
    }
}