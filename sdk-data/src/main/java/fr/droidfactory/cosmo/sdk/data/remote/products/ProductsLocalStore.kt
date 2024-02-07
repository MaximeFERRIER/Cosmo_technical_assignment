package fr.droidfactory.cosmo.sdk.data.remote.products

import fr.droidfactory.cosmo.sdk.core.models.CosmoExceptions
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.data.database.daos.ProductsDao
import fr.droidfactory.cosmo.sdk.data.mappers.toDomain
import fr.droidfactory.cosmo.sdk.data.mappers.toEntity
import javax.inject.Inject

interface ProductsLocalStore {
    suspend fun saveProducts(products: List<Product>)
    suspend fun getProducts(): List<Product>
    suspend fun getProductByMacAddress(macAddress: String): Result<Product>
}

internal class ProductsLocalStoreImpl @Inject constructor(
    private val productsDao: ProductsDao
) : ProductsLocalStore {
    override suspend fun saveProducts(products: List<Product>) {
        productsDao.deleteAll()
        productsDao.upsertAll(products.toEntity())
    }

    override suspend fun getProducts(): List<Product> = productsDao.getAllProducts().toDomain()
    override suspend fun getProductByMacAddress(macAddress: String): Result<Product> {
        val productEntity = productsDao.getProductByMacAddress(macAddress) ?: return Result.failure(
            CosmoExceptions.NoDataFound)
        return Result.success(productEntity.toDomain())
    }
}