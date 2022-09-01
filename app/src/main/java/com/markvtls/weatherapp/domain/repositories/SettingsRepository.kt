package com.markvtls.weatherapp.domain.repositories

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun saveMetricSettings(metric: String)


    fun getMetricSettings(): Flow<String>



}