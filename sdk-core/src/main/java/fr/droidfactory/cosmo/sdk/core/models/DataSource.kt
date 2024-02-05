package fr.droidfactory.cosmo.sdk.core.models

data class DataSource <T> (
    val source: Source,
    val data: T
) {
    sealed interface Source {
        data object Network: Source
        data class Database(val networkException: Throwable): Source
    }
}
