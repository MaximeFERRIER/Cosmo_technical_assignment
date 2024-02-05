package fr.droidfactory.cosmo.sdk.domain.products

import fr.droidfactory.cosmo.sdk.core.CosmoExceptions
import fr.droidfactory.cosmo.sdk.core.models.DataSource
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsDataStore
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsLocalStore
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productDataStore: ProductsDataStore,
    private val productsLocalStore: ProductsLocalStore
) : ProductRepository {

    override suspend fun getProductList(): Result<DataSource<List<Product>>> {
        val productsResponse = productDataStore.getProducts()

        return if (productsResponse.isSuccess && productsResponse.getOrNull() != null) {
            val products = requireNotNull(productsResponse.getOrNull())
            productsLocalStore.saveProducts(products = products)
            Result.success(
                DataSource(
                    source = DataSource.Source.Network,
                    data = products
                )
            )
        } else {
            val products = productsLocalStore.getProducts()
            if (products.isNotEmpty()) {
                Result.success(
                    DataSource(
                        source = DataSource.Source.Database(
                            productsResponse.exceptionOrNull() ?: CosmoExceptions.NoDataFound
                        ),
                        data = products
                    )
                )
            } else {
                Result.failure(productsResponse.exceptionOrNull() ?: Exception())
            }
        }
    }
}