package fr.droidfactory.cosmo.sdk.designsystem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.droidfactory.cosmo.sdk.designsystem.R
import fr.droidfactory.cosmo.sdk.designsystem.components.DsButton
import fr.droidfactory.cosmo.sdk.designsystem.components.DsTexts

@Composable
fun DsErrorScreen(
    title: String,
    subTitle: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(model = R.drawable.ic_error, contentDescription = "")

        Spacer(modifier = Modifier.height(8.dp))

        DsTexts.HeadlineLarge(title = title)

        Spacer(modifier = Modifier.height(8.dp))

        DsTexts.HeadlineLarge(title = subTitle)

        if (buttonText != null && onButtonClick != null) {
            DsButton.ErrorButton(text = buttonText) {
                onButtonClick()
            }
        }
    }
}