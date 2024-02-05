package fr.droidfactory.cosmo.sdk.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.droidfactory.cosmo.sdk.data.BuildConfig
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsDataStore
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsDataStoreImpl
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsLocalStore
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsLocalStoreImpl
import fr.droidfactory.cosmo.sdk.data.remote.products.ProductsService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @get:Binds
    internal abstract val ProductsDataStoreImpl.bindProductsDataStore: ProductsDataStore

    @get:Binds
    internal abstract val ProductsLocalStoreImpl.bindProductsLocalStore: ProductsLocalStore

    companion object {
        @Provides
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .readTimeout(4L, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    )
                }
            }
            .build()

        @Provides
        fun provideRetrofitClient(
            okHttpClient: OkHttpClient
        ): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.HOST)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

        @Provides
        fun provideProductsService(
            retrofit: Retrofit
        ): ProductsService = retrofit.create(ProductsService::class.java)
    }

}