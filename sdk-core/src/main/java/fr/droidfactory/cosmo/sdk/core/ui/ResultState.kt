package fr.droidfactory.cosmo.sdk.core.ui

sealed class ResultState<out T> {
    data object Uninitialized: ResultState<Nothing>()
    data object Loading: ResultState<Nothing>()
    data class Failure(val exception: Throwable): ResultState<Nothing>()
    data class Success<out T>(val data: T): ResultState<T>()

    fun errorOrNull(): Throwable? = (this as? Failure)?.exception
    fun getOrNull(): T? = (this as? Success)?.data

}