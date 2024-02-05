package fr.droidfactory.cosmo.sdk.domain.products

import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsDataStore
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsLocalStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productDataStore: ProductsDataStore,
    private val productsLocalStore: ProductsLocalStore
) : ProductRepository {
    override suspend fun updateProductList(): Result<Unit> {
        val productsResponse = productDataStore.getProducts()
        if (productsResponse.isFailure || productsResponse.getOrNull() == null) return Result.failure(
            productsResponse.exceptionOrNull() ?: Exception()
        )
        val products = requireNotNull(productsResponse.getOrNull())
        productsLocalStore.saveProducts(products)
        return Result.success(Unit)
    }

    override suspend fun observeProducts(): Flow<List<Product>> = productsLocalStore.observeProducts()
}