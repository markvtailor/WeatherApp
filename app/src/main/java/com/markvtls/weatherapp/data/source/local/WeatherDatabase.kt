package com.markvtls.weatherapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Data Base for storing API responses.
 */
@Database(entities = [Location::class,DailyForecast::class], version = 3, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        /**
         * WeatherDB instance used by all threads.
         */
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        /**
         * Getting WeatherDB instance.
         */
        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}