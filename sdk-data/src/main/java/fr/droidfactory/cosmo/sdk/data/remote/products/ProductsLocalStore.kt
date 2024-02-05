package fr.droidfactory.cosmo.sdk.data.remote.products

import fr.droidfactory.cosmo.sdk.data.mappers.toEntity
import javax.inject.Inject

interface ProductsLocalStore {
    suspend fun saveProducts(products: CosmoProductsResponse): Result<Unit>
}

internal class ProductsLocalStoreImpl @Inject constructor(

): ProductsLocalStore {
    override suspend fun saveProducts(products: CosmoProductsResponse): Result<Unit> {
        products.toEntity()
    }

}