package fr.droidfactory.cosmo.ui.products.productlist.domain

import fr.droidfactory.cosmo.sdk.core.models.DataSource
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.domain.Interactor
import fr.droidfactory.cosmo.sdk.domain.products.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class GetProductListInteractor @Inject constructor(
    dispatcherIo: CoroutineDispatcher,
    private val productRepository: ProductRepository
): Interactor<Result<DataSource<List<Product>>>>(dispatcherIo){
    override suspend fun execute(): Result<DataSource<List<Product>>> = productRepository.getProductList()
}