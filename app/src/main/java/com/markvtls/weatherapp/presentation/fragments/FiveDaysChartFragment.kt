package com.markvtls.weatherapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.markvtls.weatherapp.R
import com.markvtls.weatherapp.data.source.local.DailyForecast
import com.markvtls.weatherapp.databinding.FragmentFiveDaysChartBinding
import com.markvtls.weatherapp.presentation.WeatherViewModel
import com.markvtls.weatherapp.utils.chooseIcon
import com.markvtls.weatherapp.utils.formatDate
import com.markvtls.weatherapp.utils.getShortDayOfWeek
import com.markvtls.weatherapp.utils.translateSpeedUnit
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import kotlin.math.roundToInt

@AndroidEntryPoint
class FiveDaysChartFragment : Fragment() {
    private var _binding: FragmentFiveDaysChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()
    private val args: FiveDaysChartFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_global_weatherFragment)
                onDestroy()
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiveDaysChartBinding.inflate(inflater, container, false)
        viewModel.getLocationForecast(args.location)
        viewModel.forecastsList.observe(viewLifecycleOwner) { forecastsList ->
            val forecasts = forecastsList[0].forecasts
            val firstDay = forecasts[0]
            val secondDay = forecasts[1]
            val thirdDay = forecasts[2]
            val fourthDay = forecasts[3]
            val fifthDay = forecasts[4]
            binding.apply {
                dayOne.text = getString(R.string.icon_today_template, firstDay.date.formatDate())
                dayTwo.text = getString(R.string.icon_tomorrow_template, secondDay.date.formatDate())
                dayThree.text = getString(R.string.icon_info_template,
                    thirdDay.date.getShortDayOfWeek(),thirdDay.date.formatDate())
                dayFour.text = getString(R.string.icon_info_template,
                    fourthDay.date.getShortDayOfWeek(),fourthDay.date.formatDate())
                dayFive.text = getString(R.string.icon_info_template,
                    fifthDay.date.getShortDayOfWeek(),fifthDay.date.formatDate())
            }
            val currentTime = LocalTime.now().hour
            if (currentTime in 4..21) {
                binding.apply {
                    dayOneWind.text = getString(R.string.wind_template, firstDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayTwoWind.text = getString(R.string.wind_template, secondDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayThreeWind.text = getString(R.string.wind_template, thirdDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFourWind.text = getString(R.string.wind_template, fourthDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFiveWind.text = getString(R.string.wind_template, fifthDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                }
            } else {
                binding.apply {
                    dayOneWind.text = getString(R.string.wind_template, firstDay.night.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayTwoWind.text = getString(R.string.wind_template, secondDay.night.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayThreeWind.text = getString(R.string.wind_template, thirdDay.night.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFourWind.text = getString(R.string.wind_template, fourthDay.night.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFiveWind.text = getString(R.string.wind_template, fifthDay.night.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                }
            }
            binding.apply {
                dayOneWind.text = getString(R.string.wind_template, firstDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                dayTwoWind.text = getString(R.string.wind_template, secondDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                dayThreeWind.text = getString(R.string.wind_template, thirdDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                dayFourWind.text = getString(R.string.wind_template, fourthDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
                dayFiveWind.text = getString(R.string.wind_template, fifthDay.day.Wind.Speed.Value.toString(), firstDay.day.Wind.Speed.Unit.translateSpeedUnit())
            }
            binding.apply {
                dayOneIcon.setImageResource(firstDay.day.Icon.chooseIcon())
                dayTwoIcon.setImageResource(secondDay.day.Icon.chooseIcon())
                dayThreeIcon.setImageResource(thirdDay.day.Icon.chooseIcon())
                dayFourIcon.setImageResource(fourthDay.day.Icon.chooseIcon())
                dayFiveIcon.setImageResource(fifthDay.day.Icon.chooseIcon())

                nightOneIcon.setImageResource(firstDay.night.Icon.chooseIcon())
                nightTwoIcon.setImageResource(secondDay.night.Icon.chooseIcon())
                nightThreeIcon.setImageResource(thirdDay.night.Icon.chooseIcon())
                nightFourIcon.setImageResource(fourthDay.night.Icon.chooseIcon())
                nightFiveIcon.setImageResource(fifthDay.night.Icon.chooseIcon())
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_weatherFragment)
        }

        viewModel.forecastsList.observe(viewLifecycleOwner) {
            drawChart(it.first().forecasts)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun drawChart(forecastsList: List<DailyForecast>) {


        val formatter = object: ValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }

        }

        val maximum = ArrayList<Entry>()
        val minimum = ArrayList<Entry>()

        for (day in 0..4) {
            maximum.add(Entry(day.toFloat()+1,forecastsList[day].temperature.Maximum.Value.roundToInt().toFloat()))
            minimum.add(Entry(day.toFloat()+1,forecastsList[day].temperature.Minimum.Value.roundToInt().toFloat()))
        }



        val maxTempSet = LineDataSet(maximum,"max")
        val minTempSet = LineDataSet(minimum,"min")

        maxTempSet.apply {
            valueFormatter = formatter
            color = R.color.gray
            axisDependency = YAxis.AxisDependency.LEFT
            valueTextSize = 20F
            lineWidth = 2F
        }

        minTempSet.apply {
            valueFormatter = formatter
            color = R.color.gray
            lineWidth = 2F
            axisDependency = YAxis.AxisDependency.LEFT
            valueTextSize = 20F
        }

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(maxTempSet)
        dataSets.add(minTempSet)

        val xAxis = binding.weatherChart.xAxis
        val leftAxis = binding.weatherChart.axisLeft
        val rightAxis = binding.weatherChart.axisRight

        xAxis.apply {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawLabels(false)
            valueFormatter = formatter
        }
        leftAxis.apply {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawLabels(false)
            valueFormatter = formatter
        }
        rightAxis.apply {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawLabels(false)
            valueFormatter = formatter
        }

        binding.weatherChart.apply {
            description.isEnabled = false
            fitScreen()
            legend.isEnabled = false
            data = LineData(dataSets)
            setTouchEnabled(false)
            invalidate()
        }

    }

}