package com.markvtls.weatherapp.domain.use_cases

import com.markvtls.weatherapp.data.source.local.LocationForecasts
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetForecastsForLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(locationName: String): Flow<List<LocationForecasts>> = flow {
        val forecasts = repository.getLocationForecast(locationName)
        emit(forecasts)

    }
}