package fr.droidfactory.cosmo.ui.products.productlist.domain

import fr.droidfactory.cosmo.sdk.core.models.DataSource
import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.domain.products.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetProductListInteractorTest {

    private val mockDispatcher = Dispatchers.Unconfined

    private val mockProductRepository: ProductRepository = mockk()
    private val interactor = GetProductListInteractor(mockDispatcher, mockProductRepository)
    private val mockkProductList = listOf(
        Product(
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
        ),
        Product(
            model = Product.MODEL.REMOTE,
            product = null,
            installationMode = null,
            lightValue = 4763,
            brakeLight = false,
            macAddress = "varius",
            firmwareVersion = "varius",
            lightAuto = false,
            lightMode = Product.LIGHTMODE.WARNING,
            serial = null
        )
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
    fun `Test execute - success`() = runTest {
        val expectedResult = Result.success(DataSource(
            data = mockkProductList,
            source = DataSource.Source.Network
        ))
        coEvery { mockProductRepository.getProductList() } returns expectedResult

        val result = interactor.execute()

        coVerify { mockProductRepository.getProductList() }
        assert(result == expectedResult)
    }

    @Test
    fun `Test execute - failure`() = runTest {
        val errorMessage = "Failed to fetch product list"
        val expectedErrorResult = Result.failure<DataSource<List<Product>>>(Throwable(errorMessage))
        coEvery { mockProductRepository.getProductList() } returns Result.failure(Throwable(errorMessage))

        val result = interactor.execute()

        coVerify { mockProductRepository.getProductList() }
        assert(result.isFailure)
        assertEquals(result.exceptionOrNull()?.message, expectedErrorResult.exceptionOrNull()?.message)
    }
}