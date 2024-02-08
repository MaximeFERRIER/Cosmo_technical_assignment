package fr.droidfactory.cosmo.sdk.data.mappers

import fr.droidfactory.cosmo.sdk.core.models.Product
import fr.droidfactory.cosmo.sdk.data.database.entities.ProductEntity
import fr.droidfactory.cosmo.sdk.data.products.CosmoProductsResponse
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProductMapperTest {

    @Test
    fun `Test mapping from ProductEntity to Product`() {
        val productEntity = ProductEntity(
            macAddress = "test_mac_address",
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

        val mappedProduct = productEntity.toDomain()

        assertEquals(productEntity.macAddress, mappedProduct.macAddress)
        assertEquals(Product.MODEL.VISION, mappedProduct.model)
        assertEquals(true, mappedProduct.brakeLight)
        assertEquals("1.0.0", mappedProduct.firmwareVersion)
        assertEquals(null, mappedProduct.installationMode)
        assertEquals(true, mappedProduct.lightAuto)
        assertEquals(Product.LIGHTMODE.WARNING, mappedProduct.lightMode)
        assertEquals(5000, mappedProduct.lightValue)
        assertEquals("Test Product", mappedProduct.product)
        assertEquals("1234567890", mappedProduct.serial)
    }

    @Test
    fun `Test mapping from CosmoProductsResponse to Product`() {
        val cosmoProductsResponse = CosmoProductsResponse(
            devices = listOf(
                CosmoProductsResponse.Device(
                    macAddress = "test_mac_address",
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

        val mappedProductList = cosmoProductsResponse.toDomain()

        assertEquals(1, mappedProductList.size)
        val mappedProduct = mappedProductList[0]
        assertEquals("test_mac_address", mappedProduct.macAddress)
        assertEquals(Product.MODEL.VISION, mappedProduct.model)
        assertEquals(true, mappedProduct.brakeLight)
        assertEquals("1.0.0", mappedProduct.firmwareVersion)
        assertEquals(null, mappedProduct.installationMode)
        assertEquals(true, mappedProduct.lightAuto)
        assertEquals(Product.LIGHTMODE.WARNING, mappedProduct.lightMode)
        assertEquals(5000, mappedProduct.lightValue)
        assertEquals("Test Product", mappedProduct.product)
        assertEquals("1234567890", mappedProduct.serial)
    }


}