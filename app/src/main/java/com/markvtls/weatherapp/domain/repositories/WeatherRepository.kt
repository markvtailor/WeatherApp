package com.markvtls.weatherapp.domain.repositories

import android.content.Context
import com.markvtls.weatherapp.data.dto.FiveDayForecastResponse
import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.data.source.local.DailyForecast
import com.markvtls.weatherapp.data.source.local.Location
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun saveLongitude(longitude: Double)

    suspend fun saveLatitude(latitude: Double)

    suspend fun saveLastLocation(location: String)

    suspend fun getLocationFromApi(apiKey: String, coordinates: String, language: String): LocationResponse

    suspend fun getFiveDayForecastFromApi(apiKey: String, id: String, language: String, metric: String, details: String): FiveDayForecastResponse

    fun getLongitude(): Flow<Double>

    fun getLatitude(): Flow<Double>

    fun getLastLocation(): Flow<String>

    fun getLocationForecast(locationName: String): List<LocationForecasts>

    fun insertLocation(location: Location)

    fun insertForecast(forecast: DailyForecast)
}