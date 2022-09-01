package com.markvtls.weatherapp.domain.use_cases.settings

import com.markvtls.weatherapp.domain.repositories.SettingsRepository
import javax.inject.Inject

class SaveMetricSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(metric: String) {
        when (metric) {
            "Метрическая" -> repository.saveMetricSettings("true")
            else -> repository.saveMetricSettings("false")
        }
    }
}