package com.markvtls.weatherapp.data.source.local

import android.content.Context
    import androidx.datastore.core.DataStore
    import androidx.datastore.preferences.core.*
    import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val DEFAULT = "DEFAULT"
private val LATITUDE = doublePreferencesKey("last_latitude")
private val LONGITUDE = doublePreferencesKey("last_longitude")
private val LOCATION = stringPreferencesKey("last_location")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DEFAULT
)

class DataStore(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore
    suspend fun saveLatitudeToDataStore(latitude: Double) {
        dataStore.edit { preferences ->
                preferences[LATITUDE] = latitude
        }
    }

    suspend fun saveLongitudeToDataStore(longitude: Double) {
        dataStore.edit { preferences ->
            preferences[LONGITUDE] = longitude
        }
    }

    suspend fun saveLastLocation(location: String) {
        dataStore.edit { preferences ->
            preferences[LOCATION] = location
        }
    }

    val latitudeFlow: Flow<Double> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LATITUDE] ?: 50.0
        }


    val longitudeFlow: Flow<Double> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LONGITUDE] ?: 50.0
        }

    val lastLocationFlow: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LOCATION] ?: "Пенза"
        }
}