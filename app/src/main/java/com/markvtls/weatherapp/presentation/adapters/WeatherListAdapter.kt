package com.markvtls.weatherapp.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.futured.donut.DonutSection
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.markvtls.weatherapp.data.source.local.DailyForecast
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import com.markvtls.weatherapp.databinding.FragmentWeatherItemBinding
import com.markvtls.weatherapp.utils.*
import java.text.DecimalFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class WeatherListAdapter(private val toChart: (String) -> Unit): ListAdapter<LocationForecasts,WeatherListAdapter.WeatherViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val holder = WeatherViewHolder(
            FragmentWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            toChart
        )
        return holder
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherViewHolder(private val binding: FragmentWeatherItemBinding,private val toChart: (String) -> Unit ): RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastsList: LocationForecasts) {
            val currentTime = LocalTime.now().hour
            if (currentTime in 4..21) {
                binding.apply {
                    toolbarTitle.text = forecastsList.location.locationName
                    currentTemp.text = forecastsList.forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    description.text = forecastsList.forecasts.first().day.IconPhrase
                    todayWeatherTemp.text = "${forecastsList.forecasts.first().temperature.Maximum.Value.roundToInt()} / ${forecastsList.forecasts.first().temperature.Minimum.Value.roundToInt()}"
                    tomorrowWeatherTemp.text = "${forecastsList.forecasts[1].temperature.Maximum.Value.roundToInt()} / ${forecastsList.forecasts[1].temperature.Minimum.Value.roundToInt()}"
                    thirdDayWeather.text = forecastsList.forecasts[2].date.getDayOfWeek()
                    thirdDayWeatherTemp.text = "${forecastsList.forecasts[2].temperature.Maximum.Value.roundToInt()} / ${forecastsList.forecasts[2].temperature.Minimum.Value.roundToInt()}"
                    tempFeelsLikeValue.text = forecastsList.forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    rainProbabilityValue.text = forecastsList.forecasts.first().day.RainProbability.toString()
                    windSpeedValue.text = "${forecastsList.forecasts.first().day.Wind.Speed.Value} ${forecastsList.forecasts.first().day.Wind.Speed.Unit}"
                    windDirectionValue.text = forecastsList.forecasts.first().day.Wind.Direction.Localized
                    feelingDescriptionValue.text = forecastsList.forecasts.first().feelTemperature.Maximum.Phrase
                    precipitationValue.text = forecastsList.forecasts.first().day.HasPrecipitation.yesOrNo()
                    sunRiseTime.text = forecastsList.forecasts.first().sun.Rise.getTime()
                    sunSetTime.text = forecastsList.forecasts.first().sun.Set.getTime()
                    sunRiseBar.cap = forecastsList.forecasts.first().sun.Set.getHourFromTime()
                    sunRiseBar.addAmount(
                        "",
                        currentTime.toFloat().checkHourValue(forecastsList.forecasts.first().sun.Rise.getHourFromTime()),
                        color = Color.YELLOW
                    )
                    fiveDaysForecast.setOnClickListener {
                        toChart(forecastsList.location.locationName)
                    }
                }
            } else {
                binding.apply {
                    toolbarTitle.text = forecastsList.location.locationName
                    currentTemp.text = forecastsList.forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    description.text = forecastsList.forecasts.first().night.IconPhrase
                    todayWeatherTemp.text = "${forecastsList.forecasts.first().temperature.Maximum.Value.roundToInt()} / ${forecastsList.forecasts.first().temperature.Minimum.Value.roundToInt()}"
                    tomorrowWeatherTemp.text = "${forecastsList.forecasts[1].temperature.Maximum.Value.roundToInt()} / ${forecastsList.forecasts[1].temperature.Minimum.Value.roundToInt()}"
                    thirdDayWeather.text = forecastsList.forecasts[2].date.getDayOfWeek()
                    thirdDayWeatherTemp.text = "${forecastsList.forecasts[2].temperature.Maximum.Value.roundToInt()} / ${forecastsList.forecasts[2].temperature.Minimum.Value.roundToInt()}"
                    tempFeelsLikeValue.text = forecastsList.forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    rainProbabilityValue.text = forecastsList.forecasts.first().night.RainProbability.toString()
                    windSpeedValue.text = "${forecastsList.forecasts.first().night.Wind.Speed.Value} ${forecastsList.forecasts.first().night.Wind.Speed.Unit}"
                    windDirectionValue.text = forecastsList.forecasts.first().night.Wind.Direction.Localized
                    feelingDescriptionValue.text = forecastsList.forecasts.first().feelTemperature.Maximum.Phrase
                    precipitationValue.text = forecastsList.forecasts.first().night.HasPrecipitation.yesOrNo()
                    sunRiseTime.text = forecastsList.forecasts.first().sun.Rise.getTime()
                    sunSetTime.text = forecastsList.forecasts.first().sun.Set.getTime()
                    sunRiseBar.cap = forecastsList.forecasts.first().sun.Set.getHourFromTime()
                    sunRiseBar.addAmount(
                        "",
                        currentTime.toFloat().checkHourValue(forecastsList.forecasts.first().sun.Rise.getHourFromTime()),
                        color = Color.YELLOW
                    )
                    fiveDaysForecast.setOnClickListener {
                        toChart(forecastsList.location.locationName)
                    }
                }
            }

        }
    }


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LocationForecasts>() {
            override fun areItemsTheSame(oldItem: LocationForecasts, newItem: LocationForecasts): Boolean {
                return oldItem.forecasts.containsAll(newItem.forecasts)
            }

            override fun areContentsTheSame(
                oldItem: LocationForecasts,
                newItem: LocationForecasts
            ): Boolean {
                return oldItem.forecasts == newItem.forecasts
            }

        }
    }


}