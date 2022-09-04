package com.markvtls.weatherapp.presentation


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import com.markvtls.weatherapp.domain.model.Coordinates
import com.markvtls.weatherapp.domain.use_cases.settings.GetMetricSettingsUseCase
import com.markvtls.weatherapp.domain.use_cases.weather.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for all forecasts-related fragments.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    getCoordinates: GetCoordinatesUseCase,
    private val saveCoordinates: SaveCoordinatesUseCase,
    private val getLocation: GetLocationUseCase,
    private val getForecast: GetFiveDaysForecastUseCase,
    private val deleteOldForecast: DeleteOldForecastsUseCase,
    private val insertForecast: InsertForecastUseCase,
    private val insertLocation: InsertLocationUseCase,
    private val getForecastsForLocation: GetForecastsForLocationUseCase,
    private val saveLastLocation: SaveLastLocationUseCase,
    private val getLastLocation: GetLastLocationUseCase,
    getMetricSettings: GetMetricSettingsUseCase,
) : ViewModel() {

    enum class ExceptionCases(val case: String) {
        SUCCESS("SUCCESS"),
        MISSING_CONNECTION("CONNECTION IS MISSING")
    }

    private var _status = MutableLiveData(ExceptionCases.SUCCESS)
    val status: LiveData<ExceptionCases> get() = _status

    private var _coordinates: Flow<Coordinates> = getCoordinates()
    val coordinates get() = _coordinates
    private var _lastLocation = MutableLiveData<LocationResponse>()
    val lastLocation get() = _lastLocation
    private var _forecastsList = MutableLiveData<List<LocationForecasts>>()
    val forecastsList = _forecastsList
    private val metricSettings = getMetricSettings()


    /** Saving new coordinates to Weather DataStore. */
    fun saveNewCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            coordinates.collect { savedCoordinates ->
                if (!(savedCoordinates.latitude == latitude && savedCoordinates.longitude == longitude)) {
                    saveCoordinates(latitude, longitude)
                }
            }

        }

    }

    /** Get current location from LocationsAPI. */
    fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                coordinates.collect { currentCoordinates ->
                    getLocation(currentCoordinates).collect {
                        if (lastLocation.value != it) {
                            _lastLocation.postValue(it)
                            saveLastLocation(it.LocalizedName)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _status.postValue(ExceptionCases.MISSING_CONNECTION)
                getForecastForLastLocation()
            }

        }

    }

    /** Get current last saved forecasts for location. */
    fun getForecastForLastLocation() {
        viewModelScope.launch {
            try {
                getLastLocation().collect {
                    getLocationForecast(it)
                }
            } catch (e: Exception) {
                Log.e("Forecasting", "Last location info is missing!")
            }

        }

    }

    /** Get forecasts for provided location */
    fun getLocationForecast(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getForecastsForLocation(location).collect {
                    _forecastsList.postValue(it)
                }
        }

    }

    /** Get forecasts from ForecastsAPI. */
    fun getFiveDaysForecast(location: LocationResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            metricSettings.collect { metricSettings ->
                try {
                    getForecast(location.Key, metricSettings).collect { forecastResponse ->
                        insertLocation(location)
                        deleteOldForecast(location.LocalizedName)
                        insertForecast(location.LocalizedName, forecastResponse)
                    }
                } catch (e: Exception) {
                    Log.e("Forecasting",e.stackTraceToString())
                    _status.postValue(ExceptionCases.MISSING_CONNECTION)
                }

            }
            }

        }

    /** Confirm notification success. */
    fun notificationCheck() {
        _status.postValue(ExceptionCases.SUCCESS)
    }

    }
