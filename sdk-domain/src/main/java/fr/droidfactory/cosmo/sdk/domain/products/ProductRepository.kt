package fr.droidfactory.cosmo.sdk.domain.products

import fr.droidfactory.cosmo.sdk.core.models.DataSource
import fr.droidfactory.cosmo.sdk.core.models.Product

interface ProductRepository {
    suspend fun getProductList(): Result<DataSource<List<Product>>>
    suspend fun getProductByMacAddress(macAddress: String): Result<Product>
}