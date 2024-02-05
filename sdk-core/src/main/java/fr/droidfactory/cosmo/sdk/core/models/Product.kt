package fr.droidfactory.cosmo.sdk.core.models

data class Product(
    val macAddress: String,
    val model: MODEL,
    val brakeLight: Boolean,
    val firmwareVersion: String,
    val lightAuto: Boolean,
    val lightMode: String,
) {
    enum class MODEL {
        RIDE,
        VISION,
        REMOTE
    }
}
