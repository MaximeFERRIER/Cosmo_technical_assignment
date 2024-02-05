package fr.droidfactory.cosmo.sdk.data.remote.products

import retrofit2.Response
import retrofit2.http.GET

internal interface ProductsService {
    @GET("devices")
    suspend fun getDevices(): Response<CosmoProductsResponse>
}