package com.markvtls.weatherapp.presentation


import android.util.Log
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

    private var _coordinates: Flow<Coordinates> = getCoordinates()
    val coordinates get() = _coordinates
    private var _lastLocation = MutableLiveData<LocationResponse>()
    val lastLocation get() = _lastLocation
    private var _forecastsList = MutableLiveData<List<LocationForecasts>>()
    val forecastsList = _forecastsList
    private val metricSettings = getMetricSettings()


    init {
        //getCurrentLocation()
    }


    fun saveNewCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            coordinates.collect { savedCoordinates ->
                if (!(savedCoordinates.latitude == latitude && savedCoordinates.longitude == longitude)) {
                    saveCoordinates(latitude, longitude)
                }
            }

        }

    }

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
                getForecastForLastLocation()
            }

        }

    }
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
    fun getLocationForecast(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getForecastsForLocation(location).collect {
                    _forecastsList.postValue(it)
                }
        }

    }
    fun getFiveDaysForecast(location: LocationResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            metricSettings.collect { metricSettings ->
                getForecast(location.Key, metricSettings).collect { forecastResponse ->
                    insertLocation(location)
                    deleteOldForecast(location.LocalizedName)
                    insertForecast(location.LocalizedName, forecastResponse)
                }
            }
            }

        }
    }
