package com.markvtls.weatherapp.domain.use_cases.weather

import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

/**
 * Use this to save the last location.
 */
class SaveLastLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lastLocation: String) {
        repository.saveLastLocation(lastLocation)
    }
}