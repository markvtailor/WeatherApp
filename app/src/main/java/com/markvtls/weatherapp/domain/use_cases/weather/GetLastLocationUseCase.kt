package com.markvtls.weatherapp.domain.use_cases.weather

import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use this to get the last saved location.
 */
class GetLastLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<String> = flow {
        repository.getLastLocation().collect { lastLocation ->
            emit(lastLocation)
        }
    }
}