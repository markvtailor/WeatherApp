package com.markvtls.weatherapp.di

import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import com.markvtls.weatherapp.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideSaveCoordinatesUseCase(repository: WeatherRepository): SaveCoordinatesUseCase {
        return SaveCoordinatesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetCoordinatesUseCase(repository: WeatherRepository): GetCoordinatesUseCase {
        return GetCoordinatesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetLocationUseCase(repository: WeatherRepository): GetLocationUseCase {
        return GetLocationUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetFiveDaysForecastUseCase(repository: WeatherRepository): GetFiveDaysForecastUseCase {
        return GetFiveDaysForecastUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetForecastsForLocationUseCase(repository: WeatherRepository): GetForecastsForLocationUseCase {
        return GetForecastsForLocationUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteOldForecastsUseCase(repository: WeatherRepository): DeleteOldForecastsUseCase {
        return DeleteOldForecastsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSaveLastLocationUseCase(repository: WeatherRepository): SaveLastLocationUseCase {
        return SaveLastLocationUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetLastLocationUseCase(repository: WeatherRepository): GetLastLocationUseCase {
        return GetLastLocationUseCase(repository)
    }
}