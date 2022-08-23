package com.markvtls.weatherapp.presentation


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markvtls.weatherapp.data.dto.LocationResponse
import com.markvtls.weatherapp.data.source.local.DailyForecast
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import com.markvtls.weatherapp.domain.model.Coordinates
import com.markvtls.weatherapp.domain.use_cases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCoordinates: GetCoordinatesUseCase,
    private val saveCoordinates: SaveCoordinatesUseCase,
    private val getLocation: GetLocationUseCase,
    private val getForecast: GetFiveDaysForecastUseCase,
    private val insertForecast: InsertForecastUseCase,
    private val insertLocation: InsertLocationUseCase,
    private val getForecastsForLocation: GetForecastsForLocationUseCase,
    private val saveLastLocation: SaveLastLocationUseCase,
    private val getLastLocation: GetLastLocationUseCase
) : ViewModel() {

    private var _coordinates: Flow<Coordinates> = getCoordinates()
    val coordinates get() = _coordinates
    private var _lastLocation = MutableLiveData<LocationResponse>()
    val lastLocation get() = _lastLocation
    private var _forecastsList = MutableLiveData<List<LocationForecasts>>()
    val forecastsList = _forecastsList


    init {
        getCurrentLocation()
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
                coordinates.collect { coords ->
                    getLocation(coords).collect {
                        if (lastLocation.value != it) {
                            lastLocation.postValue(it)
                            saveLastLocation(it.LocalizedName)
                        }
                    }
                }
            } catch (e: Exception) {
                getLastLocation().collect {
                    getLocationForecast(it)
                    e.printStackTrace()
                }

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
                println("called forecasting")
                getForecast(location.Key).collect { forecastResponse ->
                    insertLocation(location)
                    insertForecast(location.LocalizedName, forecastResponse)
                }
            }
        }
    }
