package fr.droidfactory.cosmo.sdk.data.remote.products

import fr.droidfactory.cosmo.sdk.data.remote.utils.executeApiCall
import javax.inject.Inject

interface ProductsDataStore {
    suspend fun getProducts(): Result<CosmoProductsResponse>
}

internal class ProductsDataStoreImpl @Inject constructor(
    private val productsService: ProductsService
) : ProductsDataStore {
    override suspend fun getProducts(): Result<CosmoProductsResponse> = executeApiCall {
        productsService.getDevices()
    }
}