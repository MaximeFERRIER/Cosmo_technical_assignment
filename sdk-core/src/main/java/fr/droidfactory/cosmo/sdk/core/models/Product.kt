package fr.droidfactory.cosmo.sdk.core.models

data class Product(
    val model: MODEL,
    val product: String?,
    val installationMode: String?,
    val lightValue: Int,

    val brakeLight: Boolean,
    val macAddress: String,
    val firmwareVersion: String,
    val lightAuto: Boolean,
    val lightMode: LIGHTMODE,
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
