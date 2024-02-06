package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DsProductItem(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes picture: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                model = picture,
                contentDescription = "",
                contentScale = ContentScale.FillWidth)

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f))
                    .padding(16.dp),
                text = title
            )
        }
    }
}