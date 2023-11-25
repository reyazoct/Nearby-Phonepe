package com.reyaz.nearbyphone.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.LocationServices
import com.reyaz.nearbyphone.base.BaseFragment
import com.reyaz.nearbyphone.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel by viewModels<HomeVM>()
    private val eventsAdapter by lazy { EventsAdapter() }

    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        if (map.all { it.value }) findNearbyPlaces()
        else showPermissionNotAllowed()
    }

    private fun showPermissionNotAllowed() {

    }

    @SuppressLint("MissingPermission")
    private fun findNearbyPlaces() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    lifecycleScope.launch {
                        viewModel.location.emit(Pair(latitude, longitude))
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.checkNearbyPlaces.setOnClickListener {
            checkPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow.collectLatest {
                    eventsAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun initUi() {
        binding.homeVM = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.eventsRv.adapter = eventsAdapter

        if (checkLocationPermission()) findNearbyPlaces()
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}