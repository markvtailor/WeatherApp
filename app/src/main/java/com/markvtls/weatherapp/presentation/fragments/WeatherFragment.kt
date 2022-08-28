package com.markvtls.weatherapp.presentation.fragments

import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.markvtls.weatherapp.R
import com.markvtls.weatherapp.databinding.FragmentWeatherBinding
import com.markvtls.weatherapp.presentation.WeatherViewModel
import com.markvtls.weatherapp.presentation.adapters.WeatherListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment() {


    private lateinit var locationManager: LocationManager
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()
    private val location: MutableLiveData<Location> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList()
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000, 100.0F, locationListener)
        location.observe(viewLifecycleOwner) { location ->
            viewModel.saveNewCoordinates(location.latitude, location.longitude)
            viewModel.getCurrentLocation() //move somewhere
        }


        viewModel.lastLocation.observe(viewLifecycleOwner) {
            viewModel.getFiveDaysForecast(it)
            viewModel.getLocationForecast(it.LocalizedName)
        }
            viewModel.coordinates.asLiveData().observe(viewLifecycleOwner) { coordinates ->
                println(coordinates.latitude)
                println(coordinates.longitude)
            }

        }


    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }

    private val locationListener = LocationListener { location ->
        //
        this.location.postValue(location)

    }

    private fun createList() {
        val viewPager = binding.viewPager
        val adapter = WeatherListAdapter {
            toChart(it)
        }
        viewPager.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            try {
                viewModel.forecastsList.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }catch (e: Exception) {

            }
        }
    }

    fun toChart(location: String) {
        val action = WeatherFragmentDirections.actionWeatherFragmentToFiveDaysChartFragment(location)
        findNavController().navigate(action)
    }
}