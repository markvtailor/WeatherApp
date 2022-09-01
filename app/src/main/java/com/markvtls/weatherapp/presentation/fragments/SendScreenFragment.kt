package com.markvtls.weatherapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.markvtls.weatherapp.databinding.FragmentSendScreenBinding
import com.markvtls.weatherapp.presentation.WeatherViewModel
import com.markvtls.weatherapp.utils.chooseDegreesUnit
import com.markvtls.weatherapp.utils.chooseIcon
import com.markvtls.weatherapp.utils.getShortDayOfWeek
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@AndroidEntryPoint
class SendScreenFragment : Fragment() {

    private var _binging: FragmentSendScreenBinding? = null
    private val binding get() = _binging!!
    private val viewModel: WeatherViewModel by viewModels()
    private val args: SendScreenFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binging = FragmentSendScreenBinding.inflate(inflater, container, false)
        viewModel.getLocationForecast(args.location)
        viewModel.forecastsList.observe(viewLifecycleOwner) { forecastsList ->
            val forecasts = forecastsList.first().forecasts
            binding.apply {
                location.text = forecasts.first().locationNameForecast
                date.text = getCurrentDate()
                tempUnit.text = forecasts.first().temperature.Maximum.Unit.chooseDegreesUnit()
                currentTemp.text = forecasts.first().temperature.Maximum.Value.roundToInt().toString()
                description.text = forecasts.first().day.IconPhrase

                dayThree.text = forecasts[2].date.getShortDayOfWeek()
                dayFour.text = forecasts[3].date.getShortDayOfWeek()
                dayFive.text = forecasts[4].date.getShortDayOfWeek()

                dayOneIcon.setImageResource(forecasts[0].day.Icon.chooseIcon())
                dayTwoIcon.setImageResource(forecasts[1].day.Icon.chooseIcon())
                dayThreeIcon.setImageResource(forecasts[2].day.Icon.chooseIcon())
                dayFourIcon.setImageResource(forecasts[3].day.Icon.chooseIcon())
                dayFiveIcon.setImageResource(forecasts[4].day.Icon.chooseIcon())

                dayOneTemp.text = "${forecasts[0].temperature.Maximum.Value.roundToInt()}° / ${forecasts.first().temperature.Minimum.Value.roundToInt()}°"
                dayTwoTemp.text = "${forecasts[1].temperature.Maximum.Value.roundToInt()}° / ${forecasts.first().temperature.Minimum.Value.roundToInt()}°"
                dayThreeTemp.text = "${forecasts[2].temperature.Maximum.Value.roundToInt()}° / ${forecasts.first().temperature.Minimum.Value.roundToInt()}°"
                dayFourTemp.text = "${forecasts[3].temperature.Maximum.Value.roundToInt()}° / ${forecasts.first().temperature.Minimum.Value.roundToInt()}°"
                dayFiveTemp.text = "${forecasts[4].temperature.Maximum.Value.roundToInt()}° / ${forecasts.first().temperature.Minimum.Value.roundToInt()}°"
            }
        }
        return binding.root
    }

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.now().format(formatter)
    }

}