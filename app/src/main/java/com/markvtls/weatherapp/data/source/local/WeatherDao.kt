package com.markvtls.weatherapp.data.source.local

import androidx.room.*


@Dao
interface WeatherDao {

    @Transaction
    @Query("SELECT * FROM Location WHERE locationName = :locationName ")
    fun getLocationForecast(locationName: String): List<LocationForecasts>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: DailyForecast)

    @Query("DELETE FROM DailyForecast WHERE locationNameForecast = :location")
    fun deleteOldForecasts(location: String)
}