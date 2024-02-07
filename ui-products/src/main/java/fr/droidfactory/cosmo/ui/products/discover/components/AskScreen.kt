package fr.droidfactory.cosmo.ui.products.discover.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.droidfactory.cosmo.sdk.designsystem.components.DsButton
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTexts

@Composable
internal fun AskScreen(
    modifier: Modifier = Modifier,
    text: String,
    textButton: String,
    onClick: () -> Unit
) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                modifier = Modifier.size(128.dp),
                model = fr.droidfactory.cosmo.sdk.designsystem.R.drawable.bluetooth,
                contentDescription = ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            DsTexts.HeadlineMedium(title = text, align = TextAlign.Center)

            Spacer(modifier = Modifier.height(16.dp))

            DsButton.PrimaryButton(text = textButton) {
                onClick()
            }
        }
    }
}