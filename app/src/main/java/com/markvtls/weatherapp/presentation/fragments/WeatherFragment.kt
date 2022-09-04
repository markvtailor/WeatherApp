package com.markvtls.weatherapp.presentation.fragments

import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.markvtls.weatherapp.data.source.local.LocationForecasts
import com.markvtls.weatherapp.databinding.FragmentWeatherBinding
import com.markvtls.weatherapp.presentation.WeatherViewModel
import com.markvtls.weatherapp.presentation.adapters.WeatherListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * This fragment is used to hold ViewPager with forecasts.
 */
@AndroidEntryPoint
class WeatherFragment : Fragment() {

    /**
     * LocationManager instance.
     */
    private lateinit var locationManager: LocationManager
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()
    private val location: MutableLiveData<Location> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        binding.root

        /**
         * Fetching last location forecasts from DB.
         */
        viewModel.getForecastForLastLocation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Observing coordinates' changes. */
        location.observe(viewLifecycleOwner) { location ->
            /** Saving new coordinates and making a request to get user's current location */
            viewModel.saveNewCoordinates(location.latitude, location.longitude)
            viewModel.getCurrentLocation()
        }


        /** Observing location changes. */
        viewModel.lastLocation.observe(viewLifecycleOwner) {
            /** Making a request to get forecast for provided location and fetching info from DB */
            viewModel.getFiveDaysForecast(it)
            viewModel.getLocationForecast(it.LocalizedName)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forecastsList.observe(viewLifecycleOwner) {
                    createList(it)
                }
            }
        }

        /** Observing errors. */
        viewModel.status.observe(viewLifecycleOwner) {
            when (it.case) {
                "SUCCESS" -> println()
                "CONNECTION IS MISSING" -> notifyAboutRequestResult(it.case)
            }
        }


        }


    override fun onResume() {
        super.onResume()
        /** Request user's coordinates from network. */
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000, 50.0F, locationListener)
    }
    override fun onPause() {
        super.onPause()
        /** Stopping LocationManager. */
        locationManager.removeUpdates(locationListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    /** Listening for user's coordinates changes. */
    private val locationListener = LocationListener { location ->
        this.location.postValue(location)

    }


    /** Initializing ViewPager */
    private fun createList(list: List<LocationForecasts>) {
        val viewPager = binding.viewPager
        val adapter = WeatherListAdapter(
            {
                toSettings()
            },
            {
                toShare(it)
            },
            {
            openWebPage(it)
            },
            {
            toChart(it)
            }
        )
        viewPager.adapter = adapter
        adapter.submitList(list)
    }

    /** Navigate to FiveDaysChartFragment */
    private fun toChart(location: String) {
        val action = WeatherFragmentDirections.actionWeatherFragmentToFiveDaysChartFragment(location)
        findNavController().navigate(action)
    }

    /** Navigate to SendScreenFragment */
    private fun toShare(location: String) {
        val action = WeatherFragmentDirections.actionWeatherFragmentToSendScreenFragment(location)
        findNavController().navigate(action)
    }

    /** Navigate to SettingsFragment */
    private fun toSettings() {
        val action = WeatherFragmentDirections.actionWeatherFragmentToSettingsFragment()
        findNavController().navigate(action)
    }

    /** Open AccuWeather website in browser */
    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }


    /** Notify user about occurred error */
    private fun notifyAboutRequestResult(cause: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                Snackbar.make(requireView(), "Ошибка: $cause", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        viewModel.notificationCheck()
    }





}