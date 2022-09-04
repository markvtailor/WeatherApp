package com.markvtls.weatherapp.domain.use_cases.weather

import com.markvtls.weatherapp.data.dto.FiveDayForecastResponse
import com.markvtls.weatherapp.data.source.local.DailyForecast
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

/**
 * Use this to insert ForecastsAPI response into WeatherDB.
 */
class InsertForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(location: String, fiveDayForecastResponse: FiveDayForecastResponse) {

        fiveDayForecastResponse.DailyForecasts.forEach {
            val forecast = DailyForecast(
                location,
                it.Date,
                fiveDayForecastResponse.Headline.MobileLink,
                it.Sun,
                it.Temperature,
                it.RealFeelTemperature,
                it.Day,
                it.Night
            )
            repository.insertForecast(forecast)
        }
    }
}