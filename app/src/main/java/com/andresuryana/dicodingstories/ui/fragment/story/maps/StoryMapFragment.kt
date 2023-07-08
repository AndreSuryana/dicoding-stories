package com.andresuryana.dicodingstories.ui.fragment.story.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.databinding.FragmentStoryMapBinding
import com.andresuryana.dicodingstories.ui.base.BaseFragment
import com.andresuryana.dicodingstories.util.Helper.formatStoryDescriptionForMaps
import com.andresuryana.dicodingstories.util.UiState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoryMapFragment : BaseFragment(), OnMapReadyCallback {

    private var _binding: FragmentStoryMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StoryMapViewModel>()

    private lateinit var gMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoryMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get map fragment
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Observer
        viewModel.getStoriesWithLocation()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiStateObserver(it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clear layout binding
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map

        // Google maps setting
        gMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
            isMyLocationButtonEnabled = true
        }
    }

    private fun uiStateObserver(state: UiState<List<Story>>) {
        when (state) {
            is UiState.Success -> {
                hideLoading()
                addStoriesToMaps(state.data)
            }

            is UiState.Error -> {
                hideLoading()
                showErrorMessage(state.message)
            }

            is UiState.Loading -> {
                showLoading()
            }
        }
    }

    private fun addStoriesToMaps(stories: List<Story>) {
        // Create bounds builder
        val latLngBuilder = LatLngBounds.Builder()

        stories.forEach { story ->
            if (story.latitude != null && story.longitude != null) {
                // Create marker
                val marker = MarkerOptions()
                    .position(LatLng(story.latitude, story.longitude))
                    .title(getString(R.string.text_posted_by_user_location, story.name))
                    .snippet(formatStoryDescriptionForMaps(story.description))

                // Add marker into maps
                gMap.addMarker(marker)

                // Include the position to bounds builder
                latLngBuilder.include(marker.position)
            }
        }

        // Animate camera to show all markers
        gMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                latLngBuilder.build(),
                resources.getDimensionPixelSize(R.dimen.padding_maps)
            )
        )
    }
}