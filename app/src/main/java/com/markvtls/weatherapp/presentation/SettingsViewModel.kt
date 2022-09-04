package com.markvtls.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markvtls.weatherapp.domain.use_cases.settings.GetMetricSettingsUseCase
import com.markvtls.weatherapp.domain.use_cases.settings.SaveMetricSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for SettingsFragment.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveMetricSettings: SaveMetricSettingsUseCase,
    getMetricSettings: GetMetricSettingsUseCase
): ViewModel() {

    private var _metricSettings = getMetricSettings()
    val metricSettings get() = _metricSettings


    /** Save chosen metric settings to Settings DataStore */
    fun saveMetric(metric: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveMetricSettings(metric)
        }
    }


}