package fr.droidfactory.cosmo.sdk.domain.products

import fr.droidfactory.cosmo.sdk.core.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun updateProductList(): Result<Unit>
    suspend fun observeProducts(): Flow<List<Product>>
}