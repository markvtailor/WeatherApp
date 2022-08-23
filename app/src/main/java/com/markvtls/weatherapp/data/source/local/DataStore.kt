package com.markvtls.weatherapp.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val DEFAULT = "DEFAULT"
private val LATITUDE = doublePreferencesKey("last_latitude")
private val LONGITUDE = doublePreferencesKey("last_longitude")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DEFAULT
)

class DataStore(context: Context) {

    suspend fun saveLatitudeToDataStore(latitude: Double, context: Context) {
        context.dataStore.edit { preferences ->
                preferences[LATITUDE] = latitude
        }
    }

    suspend fun saveLongitudeToDataStore(longitude: Double, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[LONGITUDE] = longitude
        }
    }


    val latitudeFlow: Flow<Double> = context.dataStore.data
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


    val longitudeFlow: Flow<Double> = context.dataStore.data
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
}