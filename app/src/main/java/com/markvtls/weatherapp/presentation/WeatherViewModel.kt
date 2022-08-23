package com.markvtls.weatherapp.presentation

import android.content.Context
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
    private val getForecastsForLocation: GetForecastsForLocationUseCase
) : ViewModel() {

    private var _coordinates: Flow<Coordinates> = getCoordinates()
    val coordinates get() = _coordinates
    private var _lastLocation = MutableLiveData<LocationResponse>()
    val lastLocation get() = _lastLocation
    private var _forecastsList = MutableLiveData<List<LocationForecasts>>()
    val forecastsList = _forecastsList

    /*val locationS: Flow<LocationResponse> = flow {
        coordinates.collect {
            val data = getLocation(it)
            emit(data)
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LocationResponse("294772","Пенза")
    )
*/

    init {
        getCurrentLocation()
        //getFiveDaysForecast()
    }


    fun saveNewCoordinates(latitude: Double, longitude: Double, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            coordinates.collect { savedCoordinates ->
                if (!(savedCoordinates.latitude == latitude && savedCoordinates.longitude == longitude)) {
                    saveCoordinates(latitude, longitude, context)
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
                        }
                    }
                    //getFiveDaysForecast()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun getLocationForecast() {
        viewModelScope.launch(Dispatchers.IO) {
            getForecastsForLocation("Пенза").collect {
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
