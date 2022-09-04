package com.markvtls.weatherapp.di

import android.content.Context
import com.markvtls.weatherapp.data.repositories.SettingsRepositoryImpl
import com.markvtls.weatherapp.data.repositories.WeatherRepositoryImpl
import com.markvtls.weatherapp.data.source.local.DataStore
import com.markvtls.weatherapp.data.source.local.SettingsStore
import com.markvtls.weatherapp.data.source.local.WeatherDao
import com.markvtls.weatherapp.data.source.local.WeatherDatabase
import com.markvtls.weatherapp.data.source.remote.AccuWeatherApiService
import com.markvtls.weatherapp.domain.repositories.SettingsRepository
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing local data dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDefaultsStore(@ApplicationContext context: Context): DataStore {
        return DataStore(context)
    }



    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return WeatherDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(defaultsStore: DataStore, accuWeatherApi: AccuWeatherApiService, database: WeatherDatabase): WeatherRepository {
        return WeatherRepositoryImpl(defaultsStore, accuWeatherApi,database)
    }


    @Provides
    @Singleton
    fun provideSettingsStore(@ApplicationContext context: Context): SettingsStore {
        return SettingsStore(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsStore: SettingsStore): SettingsRepository {
        return SettingsRepositoryImpl(settingsStore)
    }
}