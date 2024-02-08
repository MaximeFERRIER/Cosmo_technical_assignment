package fr.droidfactory.cosmo.sdk.data.products


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
        val installationMode: String? = null,
        @SerialName("lightAuto")
        val lightAuto: Boolean,
        @SerialName("lightMode")
        val lightMode: String? = null,
        @SerialName("lightValue")
        val lightValue: Int,
        @SerialName("macAddress")
        val macAddress: String,
        @SerialName("model")
        val model: String,
        @SerialName("product")
        val product: String? = null,
        @SerialName("serial")
        val serial: String? = null
    )
}