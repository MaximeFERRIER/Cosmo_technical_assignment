package fr.droidfactory.cosmo.sdk.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class Interactor<out R>(private val dispatcherIO: CoroutineDispatcher) {
    suspend operator fun invoke(): R = withContext(dispatcherIO) {
        return@withContext try {
            execute()
        } catch (e: Throwable) {
            throw e
        }
    }

    abstract suspend fun execute(): R
}

abstract class InteractorWithParams<in P, out R>(private val dispatcherIO: CoroutineDispatcher) {
    suspend operator fun invoke(params: P): R = withContext(dispatcherIO) {
        return@withContext try {
            execute(params = params)
        } catch (e: Throwable) {
            throw e
        }
    }

    abstract suspend fun execute(params: P): R
}