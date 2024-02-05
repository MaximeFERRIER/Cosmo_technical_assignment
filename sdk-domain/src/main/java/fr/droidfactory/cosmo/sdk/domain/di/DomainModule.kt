package fr.droidfactory.cosmo.sdk.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.droidfactory.cosmo.sdk.domain.products.ProductRepository
import fr.droidfactory.cosmo.sdk.domain.products.ProductRepositoryImpl


@Module
@InstallIn(SingletonComponent::class)
internal abstract class DomainModule {

    @get:Binds
    internal abstract val ProductRepositoryImpl.bindProductRepository: ProductRepository
}