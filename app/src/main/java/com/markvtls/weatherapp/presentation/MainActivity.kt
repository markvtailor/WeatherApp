package com.markvtls.weatherapp.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.markvtls.weatherapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    //private lateinit var locationManager: LocationManager
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions(REQUEST_CODE)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        //locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (permissionsGranted()) {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000, 1.0F, locationListener)
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000, 1.0F, locationListener)
        }

        /*viewModel.coordinates.asLiveData().observe(this) { coordinates ->
            println(coordinates.latitude)
            println(coordinates.longitude)
        }*/

    }

    override fun onPause() {
        super.onPause()
        //locationManager.removeUpdates(locationListener)
    }

    /*private val locationListener = LocationListener { location ->
        viewModel.saveCoordinates(location.latitude, location.longitude, applicationContext)

        println(location.latitude)
    }*/

    private fun permissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(requestCode: Int) {

            if (requestCode == REQUEST_CODE) {
                if (!permissionsGranted()) {
                    ActivityCompat.requestPermissions(
                        this,
                        REQUIRED_PERMISSIONS.toTypedArray(),
                        requestCode)
                }
            }


    }
    companion object {
        private val REQUEST_CODE = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
        )
    }
}