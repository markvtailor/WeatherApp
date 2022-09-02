package com.markvtls.weatherapp.presentation.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.markvtls.weatherapp.R
import com.markvtls.weatherapp.databinding.FragmentSendScreenBinding
import com.markvtls.weatherapp.presentation.WeatherViewModel
import com.markvtls.weatherapp.utils.chooseDegreesUnit
import com.markvtls.weatherapp.utils.chooseIcon
import com.markvtls.weatherapp.utils.getShortDayOfWeek
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
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
            val firstDay = forecasts[0]
            val secondDay = forecasts[1]
            val thirdDay = forecasts[2]
            val fourthDay = forecasts[3]
            val fifthDay = forecasts[4]

            binding.apply {
                location.text = firstDay.locationNameForecast
                date.text = getCurrentDate()
                tempUnit.text = firstDay.temperature.Maximum.Unit.chooseDegreesUnit()
                currentTemp.text = firstDay.temperature.Maximum.Value.roundToInt().toString()
                description.text = firstDay.day.IconPhrase

                dayThree.text = thirdDay.date.getShortDayOfWeek()
                dayFour.text = fourthDay.date.getShortDayOfWeek()
                dayFive.text = fifthDay.date.getShortDayOfWeek()

                dayOneIcon.setImageResource(firstDay.day.Icon.chooseIcon())
                dayTwoIcon.setImageResource(secondDay.day.Icon.chooseIcon())
                dayThreeIcon.setImageResource(thirdDay.day.Icon.chooseIcon())
                dayFourIcon.setImageResource(fourthDay.day.Icon.chooseIcon())
                dayFiveIcon.setImageResource(fifthDay.day.Icon.chooseIcon())

                dayOneTemp.text = getString(R.string.temp_template,firstDay.temperature.Maximum.Value.roundToInt().toString(),firstDay.temperature.Minimum.Value.roundToInt().toString())
                dayTwoTemp.text = getString(R.string.temp_template,secondDay.temperature.Maximum.Value.roundToInt().toString(),secondDay.temperature.Minimum.Value.roundToInt().toString())
                dayThreeTemp.text = getString(R.string.temp_template,thirdDay.temperature.Maximum.Value.roundToInt().toString(),thirdDay.temperature.Minimum.Value.roundToInt().toString())
                dayFourTemp.text = getString(R.string.temp_template,fourthDay.temperature.Maximum.Value.roundToInt().toString(),fourthDay.temperature.Minimum.Value.roundToInt().toString())
                dayFiveTemp.text = getString(R.string.temp_template,fifthDay.temperature.Maximum.Value.roundToInt().toString(),fifthDay.temperature.Minimum.Value.roundToInt().toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            binding.apply {
                sendButton.setOnClickListener {
                    shareImage(takeScreenshot(shareScreen))
                }
                cancelButton.setOnClickListener {
                    findNavController().navigate(R.id.action_global_weatherFragment)
                }
            }
        }
    }


    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.now().format(formatter)
    }

    private fun takeScreenshot(v: View): Uri {
        val screenshot = Bitmap.createBitmap(v.width,v.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        v.layout(v.left,v.top,v.right,v.bottom)
        v.draw(canvas)
        val name = UUID.randomUUID().toString() + ".png"
        val outputDir = File(requireContext().filesDir,"shared")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir,name)
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(outputFile)
            screenshot.compress(Bitmap.CompressFormat.PNG,0,outputStream)
        } finally {
            outputStream?.let {
                try {
                    it.close()
                } catch (e: Exception) {}
            }
    }
        return FileProvider.getUriForFile(requireContext(),"com.markvtls.weatherapp",outputFile)
}

    private fun shareImage(imageUri: Uri) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent,null))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binging = null
    }


}