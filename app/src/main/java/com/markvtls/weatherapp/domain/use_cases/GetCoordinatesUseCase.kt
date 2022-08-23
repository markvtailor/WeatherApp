package com.markvtls.weatherapp.domain.use_cases

import com.markvtls.weatherapp.domain.model.Coordinates
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCoordinatesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<Coordinates> = flow {
        repository.getLatitude().collect { latitude ->
            repository.getLongitude().collect { longitude ->
                val coordinates = Coordinates(latitude, longitude)
                emit(coordinates)
            }
        }
    }
}