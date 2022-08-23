package com.markvtls.weatherapp.di

import com.markvtls.weatherapp.data.source.remote.AccuWeatherApiService
import com.markvtls.weatherapp.utils.Constants.ACCUWEATHER_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideAccuWeatherApiService(moshi: Moshi): AccuWeatherApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(ACCUWEATHER_BASE_URL)
            .build()
            .create(AccuWeatherApiService::class.java)
    }
}