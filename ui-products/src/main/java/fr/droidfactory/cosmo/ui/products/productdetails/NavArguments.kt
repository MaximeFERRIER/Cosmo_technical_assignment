package fr.droidfactory.cosmo.ui.products.productdetails

import androidx.lifecycle.SavedStateHandle

const val MAC_ADDRESS = "macaddress"
internal inline val SavedStateHandle.macaddress get() = get<String>(MAC_ADDRESS)!!