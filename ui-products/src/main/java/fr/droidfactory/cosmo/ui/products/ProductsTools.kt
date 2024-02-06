package fr.droidfactory.cosmo.ui.products

import fr.droidfactory.cosmo.sdk.core.models.Product

internal fun Product.MODEL.getIllustration(): Int {
    return when(this) {
        Product.MODEL.RIDE -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_ride
        Product.MODEL.VISION -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_vision
        Product.MODEL.REMOTE -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_remote
        Product.MODEL.UNKNOWN -> fr.droidfactory.cosmo.sdk.designsystem.R.drawable.cosmo_logo
    }
}