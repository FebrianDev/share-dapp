package com.febrian.sharedapps.di

import com.facebook.shimmer.BuildConfig
import com.febrian.sharedapps.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    //Provide Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder().baseUrl("http://192.168.1.13:3003")
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }

    //Provide Api Service
    @Provides
    @Singleton
    fun provideAPi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}