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
import com.markvtls.weatherapp.utils.formatDate
import com.markvtls.weatherapp.utils.getShortDayOfWeek
import dagger.hilt.android.AndroidEntryPoint
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
        val dates = listOf (
            "Сегодня, ${forecastsList[0].date.formatDate()}",
            "Завтра, ${forecastsList[1].date.formatDate()}",
            "${forecastsList[2].date.getShortDayOfWeek()}, ${forecastsList[2].date.formatDate()}",
            "${forecastsList[3].date.getShortDayOfWeek()}, ${forecastsList[3].date.formatDate()}",
            "${forecastsList[4].date.getShortDayOfWeek()}, ${forecastsList[4].date.formatDate()}"
        )

        val formatter = object: ValueFormatter() {

            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return dates[value.toInt()-1]
            }

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
        }

        minTempSet.apply {
            valueFormatter = formatter
            color = R.color.gray
            axisDependency = YAxis.AxisDependency.LEFT
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
            position = XAxis.XAxisPosition.TOP
            granularity = 1f
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