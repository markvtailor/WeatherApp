@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection")

package com.markvtls.weatherapp.data.source.remote

import com.markvtls.weatherapp.data.dto.FiveDayForecastResponse
import com.markvtls.weatherapp.data.dto.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service for making requests to AccuWeather.
 */
interface AccuWeatherApiService {

    /**
     * Get location from LocationsAPI by coordinates.
     */
    @GET("locations/v1/cities/geoposition/search?")
    suspend fun getLocationByCoords(@Query("apikey") key: String, @Query("q") coordinates: String, @Query("language") language: String): LocationResponse

    /**
     * Get forecasts for location from ForecastsAPI.
     */
    @GET("forecasts/v1/daily/5day/{id}")
    suspend fun getFiveDayForecast(@Path("id") id: String,
                                   @Query("apikey") key: String,
                                   @Query("language") language: String,
                                   @Query("metric") metric: String,
                                   @Query("details") details: String): FiveDayForecastResponse
}