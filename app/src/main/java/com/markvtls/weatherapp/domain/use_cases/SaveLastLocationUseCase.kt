package com.markvtls.weatherapp.domain.use_cases

import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

class SaveLastLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lastLocation: String) {
        repository.saveLastLocation(lastLocation)
    }
}