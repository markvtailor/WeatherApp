package com.markvtls.weatherapp.data.source.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.markvtls.weatherapp.data.dto.Day
import com.markvtls.weatherapp.data.dto.RealFeelTemperature
import com.markvtls.weatherapp.data.dto.Sun
import com.markvtls.weatherapp.data.dto.Temperature

/**
 * Entity for storing ForecastsAPI responses.
 */
@Entity(primaryKeys = ["locationNameForecast","date"])
data class DailyForecast (


    val locationNameForecast: String,
    val date: String,
    val link: String,
    @Embedded(prefix = "sun_") val sun: Sun,
    @Embedded val temperature: Temperature,
    @Embedded val feelTemperature: RealFeelTemperature,
    @Embedded(prefix = "day_") val day: Day,
    @Embedded(prefix = "night_") val night: Day

        )

/**
 * Entity for storing LocationsAPI responses.
 */
@Entity
data class Location(
    @PrimaryKey(autoGenerate = false) val locationName: String,
    val locationCode: String
)

/**
 * One-to-many relationship between Locations and Forecasts tables.
 */
data class LocationForecasts(
    @Embedded val location: Location,
    @Relation(
        parentColumn = "locationName",
        entityColumn = "locationNameForecast"
    )
    val forecasts: List<DailyForecast>
)