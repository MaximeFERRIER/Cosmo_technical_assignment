package fr.droidfactory.cosmo.sdk.data.mappers

import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.data.database.entities.ProductEntity
import fr.droidfactory.cosmo.sdk.data.remote.products.CosmoProductsResponse

/*internal fun CosmoProductsResponse.toEntity(): List<ProductEntity> = devices.map {
    it.toEntity()
}

private fun CosmoProductsResponse.Device.toEntity(): ProductEntity {
    return with(this) {
        ProductEntity(
            macAddress = macAddress,
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
}*/

internal fun List<ProductEntity>.toDomain(): List<Product> = this.map { it.toDomain() }
private fun ProductEntity.toDomain(): Product = with(this) {
    Product(
        macAddress = macAddress,
        model = Product.MODEL.entries.find { it.name == model } ?: Product.MODEL.UNKNOWN,
        brakeLight = brakeLight,
        firmwareVersion = firmwareVersion,
        lightAuto = lightAuto,
        lightMode = lightMode,
        installationMode = installationMode,
        lightValue = lightValue,
        product = product,
        serial = serial
    )
}

internal fun CosmoProductsResponse.toDomain(): List<Product> = this.devices.map {
    it.toDomain()
}

private fun CosmoProductsResponse.Device.toDomain(): Product = with(this) {
    Product(
        macAddress = macAddress,
        model = Product.MODEL.entries.find { it.name == model } ?: Product.MODEL.UNKNOWN,
        brakeLight = brakeLight,
        firmwareVersion = firmwareVersion,
        lightAuto = lightAuto,
        lightMode = lightMode,
        installationMode = installationMode,
        lightValue = lightValue,
        product = product,
        serial = serial
    )
}

internal fun List<Product>.toEntity(): List<ProductEntity> = this.map { it.toEntity() }
private fun Product.toEntity(): ProductEntity = with(this) {
    ProductEntity(
        macAddress = macAddress,
        brakeLight = brakeLight,
        firmwareVersion = firmwareVersion,
        installationMode = installationMode,
        lightAuto = lightAuto,
        lightMode = lightMode,
        lightValue = lightValue,
        model = model.name,
        product = product,
        serial = serial
    )
}