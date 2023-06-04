package com.febrian.sharedapps.di

import android.content.Context
import com.febrian.sharedapps.utils.Helper
import com.febrian.sharedapps.utils.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    //Utils
    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context) = PreferenceManager(context)

    @Provides
    @Singleton
    fun provideHelper(@ApplicationContext context: Context) = Helper(context)
}