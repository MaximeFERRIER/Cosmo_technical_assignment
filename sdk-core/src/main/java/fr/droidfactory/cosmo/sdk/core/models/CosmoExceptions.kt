package fr.droidfactory.cosmo.sdk.core.models

sealed class CosmoExceptions: Exception() {
    data object NoNetworkException: CosmoExceptions()
    data object ServerException: CosmoExceptions()
    data class GenericException(override val message: String?, val code: Int): CosmoExceptions()
    data object NoDataFound: CosmoExceptions() {
        private fun readResolve(): Any = NoDataFound
    }

    data object MissingPermissionsException: CosmoExceptions()
    data object FailedPairDeviceException: CosmoExceptions()

}