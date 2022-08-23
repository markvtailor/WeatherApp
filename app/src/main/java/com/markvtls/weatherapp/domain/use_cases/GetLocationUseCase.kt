package com.markvtls.weatherapp.domain.use_cases

import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.domain.model.Coordinates
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import com.markvtls.weatherapp.utils.Constants.ACCUWEATHER_API_KEY
import com.markvtls.weatherapp.utils.Constants.TARGET_LANGUAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(coordinates: Coordinates): Flow<LocationResponse> = flow {
        val coords = "${coordinates.latitude},${coordinates.longitude}"
        val response = repository.getLocationFromApi(ACCUWEATHER_API_KEY, coords, TARGET_LANGUAGE)
        emit(response)
    }
}