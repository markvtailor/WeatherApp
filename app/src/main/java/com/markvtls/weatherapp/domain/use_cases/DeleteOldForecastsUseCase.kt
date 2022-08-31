package com.markvtls.weatherapp.domain.use_cases

import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

class DeleteOldForecastsUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(location: String) {
        repository.deleteOldForecasts(location)
    }
}