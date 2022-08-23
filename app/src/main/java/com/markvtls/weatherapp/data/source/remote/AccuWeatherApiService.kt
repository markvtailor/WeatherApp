package com.markvtls.weatherapp.data.source.remote

import com.markvtls.weatherapp.data.dto.FiveDayForecastResponse
import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.domain.model.Coordinates
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccuWeatherApiService {


    @GET("locations/v1/cities/geoposition/search?")
    suspend fun getLocationByCoords(@Query("apikey") key: String, @Query("q") coordinates: String, @Query("language") language: String): LocationResponse

    @GET("forecasts/v1/daily/5day/{id}")
    suspend fun getFiveDayForecast(@Path("id") id: String,
                                   @Query("apikey") key: String,
                                   @Query("language") language: String,
                                   @Query("metric") metric: String,
                                   @Query("details") details: String): FiveDayForecastResponse
}