package com.markvtls.weatherapp.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.markvtls.weatherapp.R
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import com.markvtls.weatherapp.databinding.FragmentWeatherItemBinding
import com.markvtls.weatherapp.utils.*
import java.time.LocalTime
import kotlin.math.roundToInt


/**
 * Adapter for WeatherFragment ViewPager.
 */
class WeatherListAdapter(private val toSettings: (String) -> Unit,
                         private val toShare: (String) -> Unit,
                         private val openUrl: (String) -> Unit,
                         private val toChart: (String) -> Unit): ListAdapter<LocationForecasts,WeatherListAdapter.WeatherViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            FragmentWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            toSettings,
            toShare,
            toChart,
            openUrl
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    class WeatherViewHolder(private val binding: FragmentWeatherItemBinding,
                            private val toSettings: (String) -> Unit,
                            private val toShare: (String) -> Unit,
                            private val toChart: (String) -> Unit,
                            private val openUrl: (String) -> Unit ): RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastsList: LocationForecasts) {
            val forecasts = forecastsList.forecasts
            val context = itemView.context
            val today = forecasts[0]
            val tomorrow = forecasts[1]
            val thirdDay = forecasts[2]
            val currentTime = LocalTime.now().hour
            if (currentTime in 4..21) {
                binding.apply {
                    toolbarTitle.text = forecastsList.location.locationName
                    tempUnit.text = today.temperature.Maximum.Unit.chooseDegreesUnit()
                    currentTemp.text = today.temperature.Maximum.Value.roundToInt().toString()
                    description.text = today.day.IconPhrase
                    todayWeatherTemp.text = context.getString(R.string.temp_template,today.temperature.Maximum.Value.roundToInt().toString(),today.temperature.Minimum.Value.roundToInt().toString())
                    thirdDayWeatherTemp.text = context.getString(R.string.temp_template,thirdDay.temperature.Maximum.Value.roundToInt().toString(),thirdDay.temperature.Minimum.Value.roundToInt().toString())
                    tomorrowWeatherTemp.text = context.getString(R.string.temp_template,tomorrow.temperature.Maximum.Value.roundToInt().toString(),tomorrow.temperature.Minimum.Value.roundToInt().toString())
                    thirdDayWeather.text = thirdDay.date.getDayOfWeek()
                    tempFeelsLikeValue.text = context.getString(R.string.degrees,today.temperature.Maximum.Value.roundToInt().toString())
                    rainProbabilityValue.text = today.day.RainProbability.toString()
                    windSpeedValue.text = context.getString(R.string.wind_template,today.day.Wind.Speed.Value.toString(),today.day.Wind.Speed.Unit.translateSpeedUnit())
                    windDirectionValue.text = today.day.Wind.Direction.Localized
                    feelingDescriptionValue.text = today.feelTemperature.Maximum.Phrase
                    precipitationValue.text = today.day.HasPrecipitation.yesOrNo()
                    sunRiseTime.text = today.sun.Rise.getTime()
                    sunSetTime.text = today.sun.Set.getTime()
                    sunRiseBar.cap = today.sun.Set.getHourFromTime()
                    sunRiseBar.addAmount(
                        "",
                        currentTime.toFloat().checkHourValue(today.sun.Rise.getHourFromTime()),
                        color = Color.YELLOW
                    )
                    fiveDaysForecast.setOnClickListener {
                        toChart(forecastsList.location.locationName)
                    }

                    link.setOnClickListener {
                        openUrl(today.link)
                    }

                    popupMenu.setOnClickListener {
                        PopupMenu(itemView.context, it).apply {
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.settings -> {
                                        toSettings("")
                                        true
                                    }
                                    R.id.share -> {
                                        toShare(forecastsList.location.locationName)
                                        true
                                    }
                                    else -> false
                                }
                            }
                            inflate(R.menu.weather_menu)
                            show()
                        }

                    }
                    todayWeatherIcon.setImageResource(today.day.Icon.chooseIcon())
                    tomorrowWeatherIcon.setImageResource(tomorrow.day.Icon.chooseIcon())
                    thirdDayWeatherIcon.setImageResource(thirdDay.day.Icon.chooseIcon())

                }
            } else {
                binding.apply {
                    toolbarTitle.text = forecastsList.location.locationName
                    currentTemp.text = today.temperature.Maximum.Value.roundToInt().toString()
                    tempUnit.text = today.temperature.Maximum.Unit.chooseDegreesUnit()
                    description.text = today.night.IconPhrase
                    todayWeatherTemp.text = context.getString(R.string.temp_template,today.temperature.Maximum.Value.roundToInt().toString(),today.temperature.Minimum.Value.roundToInt().toString())
                    thirdDayWeatherTemp.text = context.getString(R.string.temp_template,thirdDay.temperature.Maximum.Value.roundToInt().toString(),thirdDay.temperature.Minimum.Value.roundToInt().toString())
                    tomorrowWeatherTemp.text = context.getString(R.string.temp_template,tomorrow.temperature.Maximum.Value.roundToInt().toString(),tomorrow.temperature.Minimum.Value.roundToInt().toString())
                    thirdDayWeather.text = thirdDay.date.getDayOfWeek()
                    tempFeelsLikeValue.text = context.getString(R.string.degrees,today.temperature.Maximum.Value.roundToInt().toString())
                    rainProbabilityValue.text = today.night.RainProbability.toString()
                    windSpeedValue.text = context.getString(R.string.wind_template,today.day.Wind.Speed.Value.toString(),today.day.Wind.Speed.Unit.translateSpeedUnit())
                    windDirectionValue.text = today.night.Wind.Direction.Localized
                    feelingDescriptionValue.text = today.feelTemperature.Maximum.Phrase
                    precipitationValue.text = today.night.HasPrecipitation.yesOrNo()
                    sunRiseTime.text = today.sun.Rise.getTime()
                    sunSetTime.text = today.sun.Set.getTime()
                    sunRiseBar.cap = today.sun.Set.getHourFromTime()
                    sunRiseBar.addAmount(
                        "",
                        currentTime.toFloat().checkHourValue(today.sun.Rise.getHourFromTime()),
                        color = Color.YELLOW
                    )
                    fiveDaysForecast.setOnClickListener {
                        toChart(forecastsList.location.locationName)
                    }

                    link.setOnClickListener {
                        openUrl(today.link)
                    }

                    popupMenu.setOnClickListener {
                        PopupMenu(context, it).apply {
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.settings -> {
                                        toSettings("")
                                        true
                                    }
                                    R.id.share -> {
                                        toShare(forecastsList.location.locationName)
                                        true
                                    }
                                    else -> false
                                }
                            }
                            inflate(R.menu.weather_menu)
                            show()
                        }

                    }
                    todayWeatherIcon.setImageResource(today.day.Icon.chooseIcon())
                    tomorrowWeatherIcon.setImageResource(tomorrow.day.Icon.chooseIcon())
                    thirdDayWeatherIcon.setImageResource(thirdDay.day.Icon.chooseIcon())
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