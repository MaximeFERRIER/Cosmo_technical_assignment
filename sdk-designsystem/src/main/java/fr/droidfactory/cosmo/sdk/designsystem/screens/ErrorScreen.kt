package fr.droidfactory.cosmo.sdk.designsystem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    onCloseClicked: (() -> Unit)? = null
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        onCloseClicked?.let {
            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                onClick = {
                    it()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(modifier = Modifier.size(100.dp), model = R.drawable.ic_error, contentDescription = "")

            Spacer(modifier = Modifier.height(8.dp))

            DsTexts.HeadlineLarge(title = title, align = TextAlign.Center, maxLines = 2)

            Spacer(modifier = Modifier.height(8.dp))

            DsTexts.BodyLarge(title = subTitle, align = TextAlign.Center, maxLines = 2)

            if (buttonText != null && onButtonClick != null) {
                DsButton.ErrorButton(text = buttonText) {
                    onButtonClick()
                }
            }
        }
    }
}