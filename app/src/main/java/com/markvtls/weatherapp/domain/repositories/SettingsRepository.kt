package com.markvtls.weatherapp.domain.repositories

import kotlinx.coroutines.flow.Flow
/**
 *
 *
 * SettingsRepository interface.
 */
interface SettingsRepository {

    /**
     * Save chosen metric system to Settings DataStore.
     */
    suspend fun saveMetricSettings(metric: String)

    /**
     * Get chosen metric system from Settings DataStore.
     */
    fun getMetricSettings(): Flow<String>



}