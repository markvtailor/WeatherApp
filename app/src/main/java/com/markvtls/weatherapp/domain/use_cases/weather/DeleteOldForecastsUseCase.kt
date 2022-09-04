package com.markvtls.weatherapp.domain.use_cases.weather

import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

/**
 * Use this to delete outdated forecasts.
 */
class DeleteOldForecastsUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(location: String) {
        repository.deleteOldForecasts(location)
    }
}