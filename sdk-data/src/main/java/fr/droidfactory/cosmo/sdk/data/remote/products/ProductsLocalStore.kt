package fr.droidfactory.cosmo.sdk.data.remote.products

import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.data.database.daos.ProductsDao
import fr.droidfactory.cosmo.sdk.data.mappers.toDomain
import fr.droidfactory.cosmo.sdk.data.mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProductsLocalStore {
    suspend fun saveProducts(products: CosmoProductsResponse)
    suspend fun observeProducts(): Flow<List<Product>>
}

internal class ProductsLocalStoreImpl @Inject constructor(
    private val dispatcherIO: CoroutineDispatcher,
    private val productsDao: ProductsDao
) : ProductsLocalStore {
    override suspend fun saveProducts(products: CosmoProductsResponse) {
        productsDao.deleteAll()
        productsDao.updateAll(products.toEntity())
    }

    override suspend fun observeProducts(): Flow<List<Product>> =
        productsDao.observeProducts().map { it.toDomain() }.flowOn(dispatcherIO)
}