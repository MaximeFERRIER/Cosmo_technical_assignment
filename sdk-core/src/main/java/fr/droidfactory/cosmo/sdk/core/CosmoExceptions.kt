package fr.droidfactory.cosmo.sdk.core

sealed class CosmoExceptions: Exception() {
    data object NoNetworkException: CosmoExceptions()
    data object ServerException: CosmoExceptions()
    data class GenericException(override val message: String?, val code: Int): CosmoExceptions()
    data object NoDataFound: CosmoExceptions()

}