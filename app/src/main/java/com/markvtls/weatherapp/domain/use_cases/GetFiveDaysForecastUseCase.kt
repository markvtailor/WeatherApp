package com.markvtls.weatherapp.domain.use_cases

import com.markvtls.weatherapp.data.dto.FiveDayForecastResponse
import com.markvtls.weatherapp.domain.repositories.WeatherRepository
import com.markvtls.weatherapp.utils.Constants.ACCUWEATHER_API_KEY
import com.markvtls.weatherapp.utils.Constants.DETAILS
import com.markvtls.weatherapp.utils.Constants.METRIC
import com.markvtls.weatherapp.utils.Constants.TARGET_LANGUAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFiveDaysForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(id: String): Flow<FiveDayForecastResponse> = flow {
        val response = repository.getFiveDayForecastFromApi(ACCUWEATHER_API_KEY, id, TARGET_LANGUAGE, METRIC, DETAILS)

        emit(response)
    }
}