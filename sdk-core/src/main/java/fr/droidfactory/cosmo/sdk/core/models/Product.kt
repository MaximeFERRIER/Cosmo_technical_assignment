package fr.droidfactory.cosmo.sdk.core.models

data class Product(
    val macAddress: String,
    val model: MODEL,
    val brakeLight: Boolean,
    val firmwareVersion: String,
    val lightAuto: Boolean,
    val lightMode: LIGHTMODE,
    val installationMode: String?,
    val lightValue: Int,
    val product: String?,
    val serial: String?
) {
    enum class MODEL {
        RIDE,
        VISION,
        REMOTE,
        UNKNOWN
    }

    enum class LIGHTMODE {
        OFF,
        BOTH,
        WARNING,
        POSITION,
        NONE
    }

    val productCompleteName: String
        get() = buildString {
            append(model.name)
            product?.let {
                append(" - ")
                append(it)
            }
        }
}
