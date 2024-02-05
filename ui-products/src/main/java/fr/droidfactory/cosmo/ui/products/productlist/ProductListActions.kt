package fr.droidfactory.cosmo.ui.products.productlist

internal sealed interface ProductListActions {
    data class OnProductClicked(val productMacAddress: String): ProductListActions
    data object OnFabClicked: ProductListActions
    data object OnRefresh: ProductListActions
}