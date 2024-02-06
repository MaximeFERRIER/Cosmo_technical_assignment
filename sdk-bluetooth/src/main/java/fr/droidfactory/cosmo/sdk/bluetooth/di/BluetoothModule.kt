package fr.droidfactory.cosmo.sdk.bluetooth.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.droidfactory.cosmo.sdk.bluetooth.controller.BluetoothController
import fr.droidfactory.cosmo.sdk.bluetooth.controller.BluetoothControllerImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BluetoothModule {

    companion object {
        @Provides
        fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController = BluetoothControllerImpl(context)
    }
}