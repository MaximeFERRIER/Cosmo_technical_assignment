package fr.droidfactory.cosmo.sdk.domain.products

interface ProductRepository {
    suspend fun updateProductList(): Result<Unit>
}