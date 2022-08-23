package com.markvtls.weatherapp.data.dto

import androidx.room.Embedded

data class FiveDayForecastResponse (val Headline: Headline, val DailyForecasts: List<DailyForecasts>)


data class DailyForecasts (
        val Date: String,
        val Sun:Sun,
        val Temperature: Temperature,
        val RealFeelTemperature: RealFeelTemperature,
        val Day: Day,
        val Night: Day
        )


data class Headline(val MobileLink: String)
data class Sun (val Rise: String, val Set: String)
data class Temperature(@Embedded(prefix = "min_temp_") val Minimum: TemperatureValue, @Embedded(prefix = "max_temp_") val Maximum: TemperatureValue)
data class TemperatureValue(val Value: Double, val Unit: String)
data class RealFeelTemperature(@Embedded(prefix = "min_feel_temp_") val Minimum: FeelTemperatureValue, @Embedded(prefix = "max_feel_temp_") val Maximum: FeelTemperatureValue)
data class FeelTemperatureValue(val Value: Double, val Unit: String, val Phrase: String)
data class Day(
        val Icon: Int,
        val IconPhrase: String,
        val HasPrecipitation: Boolean,
        val PrecipitationProbability: Int,
        val ThunderstormProbability: Int,
        val RainProbability: Int,
        val SnowProbability: Int,
        @Embedded val Wind: Wind,
)
data class Wind(@Embedded val Speed: Speed,@Embedded val Direction: Direction)
data class Speed(val Value: Double, val Unit: String)
data class Direction(val Degrees: Int, val Localized: String, val English: String)


