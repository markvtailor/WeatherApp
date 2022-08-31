package com.markvtls.weatherapp.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import com.markvtls.weatherapp.databinding.FragmentWeatherItemBinding
import com.markvtls.weatherapp.utils.*
import java.time.LocalTime
import kotlin.math.roundToInt

class WeatherListAdapter(private val openMenu: (View) -> Unit, private val openUrl: (String) -> Unit, private val toChart: (String) -> Unit): ListAdapter<LocationForecasts,WeatherListAdapter.WeatherViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            FragmentWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            openMenu,
            toChart,
            openUrl
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherViewHolder(private val binding: FragmentWeatherItemBinding,private val openMenu: (View) -> Unit, private val toChart: (String) -> Unit, private val openUrl: (String) -> Unit ): RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastsList: LocationForecasts) {
            val forecasts = forecastsList.forecasts
            val currentTime = LocalTime.now().hour
            if (currentTime in 4..21) {
                binding.apply {
                    //toolbarTitle.text = forecastsList.location.locationName
                    currentTemp.text = forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    description.text = forecasts.first().day.IconPhrase
                    todayWeatherTemp.text = "${forecasts.first().temperature.Maximum.Value.roundToInt()} / ${forecasts.first().temperature.Minimum.Value.roundToInt()}"
                    tomorrowWeatherTemp.text = "${forecasts[1].temperature.Maximum.Value.roundToInt()} / ${forecasts[1].temperature.Minimum.Value.roundToInt()}"
                    thirdDayWeather.text = forecasts[2].date.getDayOfWeek()
                    thirdDayWeatherTemp.text = "${forecasts[2].temperature.Maximum.Value.roundToInt()} / ${forecasts[2].temperature.Minimum.Value.roundToInt()}"
                    tempFeelsLikeValue.text = forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    rainProbabilityValue.text = forecasts.first().day.RainProbability.toString()
                    windSpeedValue.text = "${forecasts.first().day.Wind.Speed.Value} ${forecasts.first().day.Wind.Speed.Unit}"
                    windDirectionValue.text = forecasts.first().day.Wind.Direction.Localized
                    feelingDescriptionValue.text = forecasts.first().feelTemperature.Maximum.Phrase
                    precipitationValue.text = forecasts.first().day.HasPrecipitation.yesOrNo()
                    sunRiseTime.text = forecasts.first().sun.Rise.getTime()
                    sunSetTime.text = forecasts.first().sun.Set.getTime()
                    sunRiseBar.cap = forecasts.first().sun.Set.getHourFromTime()
                    sunRiseBar.addAmount(
                        "",
                        currentTime.toFloat().checkHourValue(forecasts.first().sun.Rise.getHourFromTime()),
                        color = Color.YELLOW
                    )
                    fiveDaysForecast.setOnClickListener {
                        toChart(forecastsList.location.locationName)
                    }

                    link.setOnClickListener {
                        openUrl(forecasts.first().link)
                    }

                    popupMenu.setOnClickListener {
                        openMenu(it)
                    }

                }
            } else {
                binding.apply {
                    //toolbarTitle.text = forecastsList.location.locationName
                    currentTemp.text = forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    description.text = forecasts.first().night.IconPhrase
                    todayWeatherTemp.text = "${forecasts.first().temperature.Maximum.Value.roundToInt()} / ${forecasts.first().temperature.Minimum.Value.roundToInt()}"
                    tomorrowWeatherTemp.text = "${forecasts[1].temperature.Maximum.Value.roundToInt()} / ${forecasts[1].temperature.Minimum.Value.roundToInt()}"
                    thirdDayWeather.text = forecasts[2].date.getDayOfWeek()
                    thirdDayWeatherTemp.text = "${forecasts[2].temperature.Maximum.Value.roundToInt()} / ${forecasts[2].temperature.Minimum.Value.roundToInt()}"
                    tempFeelsLikeValue.text = forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                    rainProbabilityValue.text = forecasts.first().night.RainProbability.toString()
                    windSpeedValue.text = "${forecasts.first().night.Wind.Speed.Value} ${forecasts.first().night.Wind.Speed.Unit}"
                    windDirectionValue.text = forecasts.first().night.Wind.Direction.Localized
                    feelingDescriptionValue.text = forecasts.first().feelTemperature.Maximum.Phrase
                    precipitationValue.text = forecasts.first().night.HasPrecipitation.yesOrNo()
                    sunRiseTime.text = forecasts.first().sun.Rise.getTime()
                    sunSetTime.text = forecasts.first().sun.Set.getTime()
                    sunRiseBar.cap = forecasts.first().sun.Set.getHourFromTime()
                    sunRiseBar.addAmount(
                        "",
                        currentTime.toFloat().checkHourValue(forecasts.first().sun.Rise.getHourFromTime()),
                        color = Color.YELLOW
                    )
                    fiveDaysForecast.setOnClickListener {
                        toChart(forecastsList.location.locationName)
                    }

                    link.setOnClickListener {
                        openUrl(forecasts.first().link)
                    }

                    popupMenu.setOnClickListener {
                        openMenu(it)
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