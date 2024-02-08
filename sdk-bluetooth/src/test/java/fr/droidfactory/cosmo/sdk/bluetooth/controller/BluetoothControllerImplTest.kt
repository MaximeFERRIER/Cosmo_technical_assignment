package fr.droidfactory.cosmo.sdk.bluetooth.controller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.BOND_NONE
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import io.mockk.Awaits
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothControllerImplTest {

    private lateinit var context: Context
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val deviceAddress = "00:11:22:AA:BB:CC"
    private  val bluetoothDevice: BluetoothDevice = mockk {

        every { this@mockk.bondState } returns BOND_NONE
        every { this@mockk.address } returns deviceAddress
    }

    private val test = mockkClass(BluetoothDevice::class, relaxed = true)

    // Instance under test
    private lateinit var bluetoothController: BluetoothControllerImpl

    // Setup a custom dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setup() {
        context = mockk(relaxed = true)
        bluetoothManager = mockk(relaxed = true)
        bluetoothAdapter = mockk(relaxed = true)

        mockkStatic(TextUtilsWrapper::class)
        every { ContextCompat.checkSelfPermission(context, any<String>()) } returns PermissionChecker.PERMISSION_GRANTED
        every { context.getSystemService(BluetoothManager::class.java) } returns bluetoothManager
        every { bluetoothManager.adapter } returns bluetoothAdapter

        every { bluetoothAdapter.isEnabled } returns true

        bluetoothController = BluetoothControllerImpl(context)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun discovertStartStopTest() = runTest {
        assertFalse(bluetoothController.isScanning.value)
        bluetoothController.startDiscovery()

        coVerify { bluetoothAdapter.startDiscovery() }
        val isScanningValues = bluetoothController.isScanning.value
        assertTrue(isScanningValues)

        bluetoothController.stopDiscovery()
        assertFalse(bluetoothController.isScanning.value)
    }
}

class TextUtilsWrapper {
    fun equals(text1: String, text2: String) = TextUtils.equals(text1, text2)
}