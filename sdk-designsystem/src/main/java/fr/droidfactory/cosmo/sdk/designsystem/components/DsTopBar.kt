package fr.droidfactory.cosmo.sdk.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ActionButton(
    val icon: ImageVector,
    val onClick: () -> Unit
)

object DsTopBar {
    @Composable
    fun NoNavigationTopBar(
        modifier: Modifier = Modifier,
        title: String
    ) = TopAppBarImpl(modifier = modifier, title = title)

    @Composable
    fun NavigationTopBar(
        modifier: Modifier = Modifier,
        title: String,
        onNavigationClick: () -> Unit
    ) = TopAppBarImpl(modifier = modifier, title = title, onNavigationClick = onNavigationClick, navigationIcon = Icons.AutoMirrored.Filled.ArrowBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarImpl(
    modifier: Modifier,
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: List<ActionButton> = emptyList()
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(
                    onClick = {
                        onNavigationClick()
                    }) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            actions.forEach {
                IconButton(onClick = { it.onClick() }) {
                    Icon(imageVector = it.icon, contentDescription = "", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    )
}