package com.markvtls.weatherapp.domain.repositories

import com.markvtls.weatherapp.data.dto.FiveDayForecastResponse
import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.data.source.local.DailyForecast
import com.markvtls.weatherapp.data.source.local.Location
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    /**
     * Save LocationManager results to Weather DataStore.
     */
    suspend fun saveLongitude(longitude: Double)

    /**
     * Save LocationManager results to Weather DataStore.
     */
    suspend fun saveLatitude(latitude: Double)

    /**
     * Save AccuWeather LocationsAPI response Weather DataStore.
     */
    suspend fun saveLastLocation(location: String)

    /**
     * Make a request to AccuWeather LocationsAPI.
     */
    suspend fun getLocationFromApi(apiKey: String, coordinates: String, language: String): LocationResponse

    /**
     * Make a request to AccuWeather ForecastsAPI.
     */
    suspend fun getFiveDayForecastFromApi(apiKey: String, id: String, language: String, metric: String, details: String): FiveDayForecastResponse

    /**
     * Get Longitude from Weather DataStore.
     */
    fun getLongitude(): Flow<Double>

    /**
     * Get Latitude from Weather DataStore.
     */
    fun getLatitude(): Flow<Double>

    /**
     * Get the last location from Weather DataStore.
     */
    fun getLastLocation(): Flow<String>

    /**
     * Get forecasts list from Room DB by location's name.
     */
    fun getLocationForecast(locationName: String): List<LocationForecasts>

    /**
     * Insert Accuweather LocationsAPI response into Room DB.
     */
    fun insertLocation(location: Location)

    /**
     * Insert Accuweather ForecastsAPI response into Room DB.
     */
    fun insertForecast(forecast: DailyForecast)

    /**
     * Delete RoomDB rows by location's name.
     */
    fun deleteOldForecasts(location: String)
}