package com.markvtls.weatherapp.data.repositories


import com.markvtls.weatherapp.data.dto.FiveDayForecastResponse
import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.data.source.local.*
import com.markvtls.weatherapp.data.source.remote.AccuWeatherApiService
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val defaults: DataStore,
    private val accuWeatherApi: AccuWeatherApiService,
    private val database: WeatherDatabase
    ): WeatherRepository {

    override suspend fun saveLongitude(longitude: Double) {
        defaults.saveLongitudeToDataStore(longitude)
    }

    override suspend fun saveLatitude(latitude: Double) {
        defaults.saveLatitudeToDataStore(latitude)
    }

    override suspend fun saveLastLocation(location: String) {
        defaults.saveLastLocation(location)
    }

    override suspend fun getLocationFromApi(apiKey: String, coordinates: String, language: String): LocationResponse {
        return accuWeatherApi.getLocationByCoords(apiKey,coordinates,language)
    }

    override suspend fun getFiveDayForecastFromApi(
        apiKey: String,
        id: String,
        language: String,
        metric: String,
        details: String
    ): FiveDayForecastResponse {
        return accuWeatherApi.getFiveDayForecast(id,apiKey,language,metric, details)
    }

    override fun getLongitude(): Flow<Double> {
        return defaults.longitudeFlow
    }

    override fun getLatitude(): Flow<Double> {
        return defaults.latitudeFlow
    }

    override fun getLastLocation(): Flow<String> {
        return defaults.lastLocationFlow
    }

    override fun getLocationForecast(locationName: String): List<LocationForecasts> {
        return database.weatherDao().getLocationForecast(locationName)
    }

    override fun insertLocation(location: Location) {
        database.weatherDao().insertLocation(location)
    }

    override fun insertForecast(forecast: DailyForecast) {
        database.weatherDao().insertForecast(forecast)
    }

    override fun deleteOldForecasts(location: String) {
        database.weatherDao().deleteOldForecasts(location)
    }


}