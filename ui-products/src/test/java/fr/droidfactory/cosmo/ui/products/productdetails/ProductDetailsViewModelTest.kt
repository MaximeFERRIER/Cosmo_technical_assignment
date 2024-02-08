package fr.droidfactory.cosmo.ui.products.productdetails

import androidx.lifecycle.SavedStateHandle
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.core.ui.ResultState
import fr.droidfactory.cosmo.ui.products.productdetails.domain.GetProductByMacAddressInteractor
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getProductByMacAddressInteractor = GetProductByMacAddressInteractor(
        dispatcherIO = testDispatcher,
        productRepository = mockk()
    )

    private val macAddress = "test_mac_address"
    private val product = Product(
        Product.MODEL.VISION,
        null,
        null,
        100,
        false,
        macAddress,
        "",
        false,
        Product.LIGHTMODE.NONE,
        null
    )
    private val successResult = Result.success(product)

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(mapOf("macaddress" to macAddress))
    }

    @Test
    fun testGetProductSuccess() = runTest {
        coEvery { getProductByMacAddressInteractor.execute(macAddress) } returns successResult
        viewModel = ProductDetailsViewModel(getProductByMacAddressInteractor, savedStateHandle)


        val successState = viewModel.product.dropWhile { it !is ResultState.Success }.first()

        assertTrue(successState is ResultState.Success)
        assertEquals(product, successState.getOrNull())
    }

    @Test
    fun testGetProductFailure() = runTest {
        val mockError = Throwable("Network Error")
        coEvery { getProductByMacAddressInteractor.execute(macAddress) } returns Result.failure(
            mockError
        )
        viewModel = ProductDetailsViewModel(getProductByMacAddressInteractor, savedStateHandle)


        val errorState = viewModel.product.dropWhile { it !is ResultState.Failure }.first()

        assertTrue(errorState is ResultState.Failure)
        assertEquals(mockError, (errorState as ResultState.Failure).exception)

    }
}