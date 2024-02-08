package fr.droidfactory.cosmo.sdk.data.remote.products

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.droidfactory.cosmo.sdk.data.products.CosmoProductsResponse
import fr.droidfactory.cosmo.sdk.data.products.ProductsService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit

@RunWith(JUnit4::class)
class ProductsServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var productsService: ProductsService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        productsService = retrofit.create(ProductsService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Test getDevices`() = runTest {
        val mockResponseBody = CosmoProductsResponse(
            devices = listOf(
                CosmoProductsResponse.Device(
                    macAddress = "mock_mac_address",
                    model = "VISION",
                    brakeLight = true,
                    firmwareVersion = "1.0.0",
                    installationMode = null,
                    lightAuto = true,
                    lightMode = "WARNING",
                    lightValue = 5000,
                    product = "Test Product",
                    serial = "1234567890"
                )
            )
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Json.encodeToString(mockResponseBody))
        )


        val response = productsService.getDevices()

        assertEquals(200, response.code())
        val responseBody = response.body()
        assert(responseBody != null)
        assertEquals(mockResponseBody, responseBody)
    }
}