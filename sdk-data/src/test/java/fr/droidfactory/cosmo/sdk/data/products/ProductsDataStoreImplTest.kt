package fr.droidfactory.cosmo.sdk.data.products

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response


@RunWith(JUnit4::class)
class ProductsDataStoreImplTest {
    private val productsService = mockk<ProductsService>()

    private val dataStore = ProductsDataStoreImpl(productsService)

    @Test
    fun `Test getProducts - Success`() = runTest {
        val mockResponse = CosmoProductsResponse(
            devices = listOf(
                CosmoProductsResponse.Device(
                    macAddress = "mac1",
                    brakeLight = false,
                    firmwareVersion = "1.0.0",
                    installationMode = null,
                    lightAuto = true,
                    lightMode = "WARNING",
                    lightValue = 5000,
                    model = "VISION",
                    product = "Test Product",
                    serial = "1234567890"
                )
            )
        )

        coEvery { productsService.getDevices() } returns Response.success(mockResponse)

        val result = dataStore.getProducts()

        assert(result.isSuccess)
        assert(result.getOrNull() != null)
        assert(result.getOrNull()!!.size == 1)
        assert(result.getOrNull()!!.first().macAddress == "mac1")
    }

    @Test
    fun `Test getProducts - Failure`() = runTest {
        coEvery { productsService.getDevices() } throws Exception("Network error")

        val result = dataStore.getProducts()

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Network error")
    }
}