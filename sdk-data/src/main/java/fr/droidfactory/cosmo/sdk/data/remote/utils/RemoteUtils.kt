package fr.droidfactory.cosmo.sdk.data.remote.utils

import fr.droidfactory.cosmo.sdk.core.models.CosmoExceptions
import okio.IOException
import retrofit2.Response


suspend fun <T> executeApiCall(
    call: suspend () -> Response<T>
) : Result<T> {
    return try {
        val response = call()
        if(response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            val error = when {
                response.code() >= 500 -> CosmoExceptions.ServerException
                else -> CosmoExceptions.GenericException(message = response.errorBody()?.string(), code = response.code())
            }
            Result.failure(error)
        }
    }  catch (e: IOException) {
        Result.failure(CosmoExceptions.NoNetworkException)
    }  catch (npe: NullPointerException) {
        Result.failure(CosmoExceptions.NoDataFound)
    } catch (e: Exception) {
       Result.failure(e)
    }
}