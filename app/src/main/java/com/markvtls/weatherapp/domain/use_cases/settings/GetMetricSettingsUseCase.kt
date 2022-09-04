package com.markvtls.weatherapp.domain.use_cases.settings

import com.markvtls.weatherapp.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use this to get chosen metric settings.
 */
class GetMetricSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {


    operator fun invoke(): Flow<String> = flow {
        repository.getMetricSettings().collect { metricSettings ->
            emit(metricSettings)
        }
    }
}