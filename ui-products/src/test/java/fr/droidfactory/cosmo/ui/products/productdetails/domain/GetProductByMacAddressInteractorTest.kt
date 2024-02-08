package fr.droidfactory.cosmo.ui.products.productdetails.domain

import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.domain.products.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetProductByMacAddressInteractorTest {

    private val mockDispatcher = Dispatchers.Unconfined

    private val mockProductRepository: ProductRepository = mockk()
    private val interactor = GetProductByMacAddressInteractor(mockDispatcher, mockProductRepository)
    private val mockProduct = Product(
        model = Product.MODEL.VISION,
        product = "Vision",
        installationMode = null,
        lightValue = 3223,
        brakeLight = false,
        macAddress = "intellegebat",
        firmwareVersion = "dictum",
        lightAuto = false,
        lightMode = Product.LIGHTMODE.WARNING,
        serial = null
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getProductByMacAddressTest() = runTest {
        val macAddress = "testMacAddress"
        val expectedResult = Result.success(mockProduct)
        coEvery { mockProductRepository.getProductByMacAddress(macAddress) } returns expectedResult

        val result = interactor(macAddress)

        coVerify { mockProductRepository.getProductByMacAddress(macAddress) }
        assert(result == expectedResult)
    }

    @Test
    fun getProductByMacAddressFailedTest() = runTest {
        val macAddress = "testMacAddress"
        val errorMessage = "Failed to fetch product by mac address"
        val expectedErrorResult = Result.failure<Product>(Throwable(errorMessage))
        coEvery { mockProductRepository.getProductByMacAddress(macAddress) } returns Result.failure<Product>(Throwable(errorMessage))

        val result = interactor(macAddress)

        coVerify { mockProductRepository.getProductByMacAddress(macAddress) }
        assert(result.exceptionOrNull()?.message == expectedErrorResult.exceptionOrNull()?.message)
    }
}