package fr.droidfactory.cosmo.sdk.data.remote.products


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CosmoProductsResponse(
    @SerialName("devices")
    val devices: List<Device>
) {
    @Serializable
    data class Device(
        @SerialName("brakeLight")
        val brakeLight: Boolean,
        @SerialName("firmwareVersion")
        val firmwareVersion: String,
        @SerialName("installationMode")
        val installationMode: String,
        @SerialName("lightAuto")
        val lightAuto: Boolean,
        @SerialName("lightMode")
        val lightMode: String,
        @SerialName("lightValue")
        val lightValue: Int,
        @SerialName("macAddress")
        val macAddress: String,
        @SerialName("model")
        val model: String,
        @SerialName("product")
        val product: String,
        @SerialName("serial")
        val serial: String
    )
}