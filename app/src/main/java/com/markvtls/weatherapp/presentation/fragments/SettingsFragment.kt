package com.markvtls.weatherapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.markvtls.weatherapp.R
import com.markvtls.weatherapp.databinding.FragmentSettingsBinding
import com.markvtls.weatherapp.presentation.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()



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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metricChoices = binding.metricChoices
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.metrics_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            metricChoices.adapter = adapter
            viewModel.metricSettings.asLiveData().observe(viewLifecycleOwner) { metricSettings ->
                val currentSettings = when (metricSettings) {
                    "true" -> "Метрическая"
                    else -> "Имперская"
                }
                metricChoices.setSelection(adapter.getPosition(currentSettings))
            }
        }
        metricChoices.onItemSelectedListener = metricSettingsListener

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_weatherFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private val metricSettingsListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position)
            viewModel.saveMetric(item.toString())
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            println("nothing selected")
        }
    }

}