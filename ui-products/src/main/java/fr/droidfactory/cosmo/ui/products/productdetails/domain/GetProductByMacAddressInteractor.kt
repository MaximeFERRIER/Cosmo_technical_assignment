package fr.droidfactory.cosmo.ui.products.productdetails.domain

import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.domain.InteractorWithParams
import fr.droidfactory.cosmo.sdk.domain.products.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class GetProductByMacAddressInteractor @Inject constructor(
    dispatcherIO: CoroutineDispatcher,
    private val productRepository: ProductRepository
) : InteractorWithParams<String, Result<Product>>(dispatcherIO) {
    override suspend fun execute(params: String): Result<Product> =
        productRepository.getProductByMacAddress(macAddress = params)
}