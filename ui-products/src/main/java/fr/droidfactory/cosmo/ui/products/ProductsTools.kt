package fr.droidfactory.cosmo.ui.products

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.droidfactory.cosmo.sdk.core.CosmoExceptions
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.designsystem.components.DsLightItem

internal fun Product.MODEL.getIllustration(): Int {
    return when(this) {
        Product.MODEL.RIDE -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_ride
        Product.MODEL.VISION -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_vision
        Product.MODEL.REMOTE -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_remote
        Product.MODEL.UNKNOWN -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_logo
    }
}

internal sealed interface ItemInfo
data class TextItem(val title: String, val text: String): ItemInfo
data class CustomItem(val title: String, val content: @Composable () -> Unit): ItemInfo

internal fun Product.getProductDescription(context: Context): List<ItemInfo> {
    return arrayListOf<ItemInfo>().apply {
        add(TextItem(title = context.getString(R.string.model), text = model.name))
        product?.let {
            add(TextItem(title = context.getString(R.string.product), text = it))
        }

        installationMode?.let {
            add(TextItem(title = context.getString(R.string.installation_mode), text = it))
        }
    }
}

internal fun Product.getProductAbout(context: Context): List<ItemInfo> {
    return arrayListOf<ItemInfo>().apply {
        if(lightMode != Product.LIGHTMODE.NONE) {
            add(CustomItem(title = context.getString(R.string.light_mode), content = {
                val modifier = Modifier.size(20.dp)
                when(lightMode) {
                    Product.LIGHTMODE.OFF -> DsLightItem.OffLight(modifier)
                    Product.LIGHTMODE.BOTH -> DsLightItem.BothLight(modifier)
                    Product.LIGHTMODE.WARNING -> DsLightItem.WarningLight(modifier)
                    Product.LIGHTMODE.POSITION -> DsLightItem.PositionLight(modifier)
                    else -> {}
                }
            }))
            add(TextItem(title = context.getString(R.string.light_value), text = context.getString(R.string.lumen, lightValue)))
            add(TextItem(title = context.getString(R.string.light_auto), text = lightAuto.toYesNo(context)))
        }

        add(TextItem(title = context.getString(R.string.mac_address), text = macAddress))
        add(TextItem(title = context.getString(R.string.firmware_version), text = firmwareVersion))
    }
}

private fun Boolean.toYesNo(context: Context) = if(this) context.getString(R.string.yes) else context.getString(R.string.no)

internal fun Throwable.toErrorMessage(context: Context): String {
    return when(this) {
        is CosmoExceptions.GenericException -> "${context.getString(R.string.error_title)} $message"
        CosmoExceptions.NoDataFound -> context.getString(R.string.error_title)
        CosmoExceptions.NoNetworkException -> context.getString(R.string.error_offline)
        CosmoExceptions.ServerException -> context.getString(R.string.error_api)
        else -> context.getString(R.string.error_title)
    }
}