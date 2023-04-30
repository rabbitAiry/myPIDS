package com.airy.passengers_pids.di

import android.content.Context
import com.airy.pids_lib.bluetooth.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController{
        return BluetoothController(context)
    }
}