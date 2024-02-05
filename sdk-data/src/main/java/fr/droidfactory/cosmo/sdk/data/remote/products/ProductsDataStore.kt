package fr.droidfactory.cosmo.sdk.data.remote.products

import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.data.mappers.toDomain
import fr.droidfactory.cosmo.sdk.data.remote.utils.executeApiCall
import javax.inject.Inject

interface ProductsDataStore {
    suspend fun getProducts(): Result<List<Product>>
}

internal class ProductsDataStoreImpl @Inject constructor(
    private val productsService: ProductsService
) : ProductsDataStore {
    override suspend fun getProducts(): Result<List<Product>> = executeApiCall {
        productsService.getDevices()
    }.fold(onSuccess = {
        Result.success(it.toDomain())
    }, onFailure = {
        Result.failure(it)
    })
}