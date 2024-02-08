package fr.droidfactory.cosmo.ui.products.productlist

import fr.droidfactory.cosmo.sdk.core.models.DataSource
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.ui.products.productlist.domain.GetProductListInteractor
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getProductListInteractor = GetProductListInteractor(
        dispatcherIo = testDispatcher,
        productRepository = mockk()
    )

    private val productList = listOf(
        Product(
            Product.MODEL.VISION,
            null,
            null,
            100,
            false,
            "",
            "",
            false,
            Product.LIGHTMODE.NONE,
            null
        )
    )
    private val successResult =
        Result.success(DataSource(data = productList, source = DataSource.Source.Network))

    private lateinit var viewModel: ProductListViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

    }

    @Test
    fun testGetProductListSuccess() = runTest {
        coEvery { getProductListInteractor.execute() } returns successResult
        viewModel = ProductListViewModel(getProductListInteractor)
        val job = launch {
            viewModel.getProductList()
        }

        val successState = viewModel.productsState.dropWhile { it !is ResultState.Success }.first()

        assert(successState is ResultState.Success)
        assert(successState.getOrNull() == productList)

        job.cancel()
    }

    @Test
    fun testGetProductListFailure() = runTest {
        val mockError = Throwable("Network Error")
        coEvery { getProductListInteractor.execute() } returns Result.failure(mockError)
        viewModel = ProductListViewModel(getProductListInteractor)
        val job = launch {
            viewModel.getProductList()
        }

        val errorState = viewModel.productsState.dropWhile { it !is ResultState.Failure }.first()

        assertTrue(errorState is ResultState.Failure)
        assertEquals(mockError, (errorState as ResultState.Failure).exception)
        job.cancel()
    }
}