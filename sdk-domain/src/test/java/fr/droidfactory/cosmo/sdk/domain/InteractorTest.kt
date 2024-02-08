package fr.droidfactory.cosmo.sdk.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test


class InteractorTest {

    private val mockedResultWithoutParams = "Mocked result"
    private val mockedResultWithParams = "Mocked result with params 123"

    private class TestInteractor(dispatcherIO: CoroutineDispatcher) :
        Interactor<String>(dispatcherIO) {
        override suspend fun execute(): String {
            return "Mocked result"
        }
    }

    private class TestInteractorWithParams(dispatcherIO: CoroutineDispatcher) :
        InteractorWithParams<Int, String>(dispatcherIO) {
        override suspend fun execute(params: Int): String {
            return "Mocked result with params $params"
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun interactorTest() = runTest {
        val interactor = mockk<TestInteractor>()
        coEvery { interactor.invoke() } returns mockedResultWithoutParams

        val result = interactor.invoke()
        coVerify { interactor.invoke() }
        assert(result == mockedResultWithoutParams)
    }

    @Test
    fun interactorWithParamsTest() = runTest {
        val interactor = mockk<TestInteractorWithParams>()
        coEvery { interactor.invoke(123) } returns mockedResultWithParams

        val result = interactor.invoke(123)
        coVerify { interactor.invoke(123) }
        assert(result == mockedResultWithParams)
    }
}