package com.markvtls.weatherapp.domain.use_cases.weather

import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

/**
 * Use this to save current coordinates.
 */
class SaveCoordinatesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double) {
        repository.saveLatitude(latitude)
        repository.saveLongitude(longitude)
    }
}