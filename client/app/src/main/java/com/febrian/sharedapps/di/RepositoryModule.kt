package com.febrian.sharedapps.di

import com.febrian.sharedapps.data.api.ApiService
import com.febrian.sharedapps.data.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePostRepository(apiService: ApiService) = PostRepository(apiService)

}