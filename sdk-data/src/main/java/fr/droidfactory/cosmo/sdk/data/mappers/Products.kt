package fr.droidfactory.cosmo.sdk.data.mappers

import fr.droidfactory.cosmo.sdk.data.database.entities.ProductEntity
import fr.droidfactory.cosmo.sdk.data.remote.products.CosmoProductsResponse

internal fun CosmoProductsResponse.toEntity(): List<ProductEntity> = devices.map {
    it.toEntity()
}

private fun CosmoProductsResponse.Device.toEntity(): ProductEntity {
    return with(this) {
        ProductEntity(
            _id = macAddress,
            brakeLight = brakeLight,
            firmwareVersion = firmwareVersion,
            installationMode = installationMode,
            lightAuto = lightAuto,
            lightMode = lightMode,
            lightValue = lightValue,
            model = model,
            product = product,
            serial = serial
        )
    }
}