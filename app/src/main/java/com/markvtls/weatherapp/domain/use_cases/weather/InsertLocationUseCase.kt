package com.markvtls.weatherapp.domain.use_cases.weather

import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.data.source.local.Location
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

class InsertLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(locationResponse: LocationResponse) {
        val location = Location(locationResponse.LocalizedName, locationResponse.Key)
        repository.insertLocation(location)
    }
}