package com.markvtls.weatherapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiveDaysChartBinding.inflate(inflater, container, false)
        viewModel.getLocationForecast(args.location)
        viewModel.forecastsList.observe(viewLifecycleOwner) { forecastsList ->
            val forecasts = forecastsList.first().forecasts
            binding.apply {
                dayOne.text = "Cегодня \n ${forecasts[0].date.formatDate()}"
                dayTwo.text = "Завтра \n ${forecasts[1].date.formatDate()}"
                dayThree.text = "  ${forecasts[2].date.getShortDayOfWeek()} \n ${forecasts[2].date.formatDate()}"
                dayFour.text = "  ${forecasts[3].date.getShortDayOfWeek()} \n ${forecasts[3].date.formatDate()}"
                dayFive.text = "  ${forecasts[4].date.getShortDayOfWeek()} \n ${forecasts[4].date.formatDate()}"
            }
            val currentTime = LocalTime.now().hour
            if (currentTime in 4..21) {
                binding.apply {
                    dayOneWind.text = getString(R.string.wind_template, forecasts[0].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayTwoWind.text = getString(R.string.wind_template, forecasts[1].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayThreeWind.text = getString(R.string.wind_template, forecasts[2].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFourWind.text = getString(R.string.wind_template, forecasts[3].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFiveWind.text = getString(R.string.wind_template, forecasts[4].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                }
            } else {
                binding.apply {
                    dayOneWind.text = getString(R.string.wind_template, forecasts[0].night.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayTwoWind.text = getString(R.string.wind_template, forecasts[1].night.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayThreeWind.text = getString(R.string.wind_template, forecasts[2].night.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFourWind.text = getString(R.string.wind_template, forecasts[3].night.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                    dayFiveWind.text = getString(R.string.wind_template, forecasts[4].night.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                }
            }
            binding.apply {
                dayOneWind.text = getString(R.string.wind_template, forecasts[0].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                dayTwoWind.text = getString(R.string.wind_template, forecasts[1].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                dayThreeWind.text = getString(R.string.wind_template, forecasts[2].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                dayFourWind.text = getString(R.string.wind_template, forecasts[3].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
                dayFiveWind.text = getString(R.string.wind_template, forecasts[4].day.Wind.Speed.Value.toString(), forecasts[0].day.Wind.Speed.Unit.translateSpeedUnit())
            }
            binding.apply {
                dayOneIcon.setImageResource(forecasts[0].day.Icon.chooseIcon())
                dayTwoIcon.setImageResource(forecasts[1].day.Icon.chooseIcon())
                dayThreeIcon.setImageResource(forecasts[2].day.Icon.chooseIcon())
                dayFourIcon.setImageResource(forecasts[3].day.Icon.chooseIcon())
                dayFiveIcon.setImageResource(forecasts[4].day.Icon.chooseIcon())

                nightOneIcon.setImageResource(forecasts[0].night.Icon.chooseIcon())
                nightTwoIcon.setImageResource(forecasts[1].night.Icon.chooseIcon())
                nightThreeIcon.setImageResource(forecasts[2].night.Icon.chooseIcon())
                nightFourIcon.setImageResource(forecasts[3].night.Icon.chooseIcon())
                nightFiveIcon.setImageResource(forecasts[4].night.Icon.chooseIcon())
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