package com.markvtls.weatherapp.data.repositories

import com.markvtls.weatherapp.data.source.local.SettingsStore
import com.markvtls.weatherapp.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 *
 *
 * Implementation of SettingsRepository.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val settings: SettingsStore
) : SettingsRepository {

    override suspend fun saveMetricSettings(metric: String) {
        settings.saveMetricSettingsToDataStore(metric)
    }



    override fun getMetricSettings(): Flow<String> {
        return settings.metricFlow
    }


}