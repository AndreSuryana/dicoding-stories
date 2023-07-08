package com.andresuryana.dicodingstories.ui.fragment.story.add

import android.animation.AnimatorSet
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.databinding.FragmentAddStoryBinding
import com.andresuryana.dicodingstories.ui.base.ImagePickerFragment
import com.andresuryana.dicodingstories.util.AnimationHelper
import com.andresuryana.dicodingstories.util.Ext.enableMyLocation
import com.andresuryana.dicodingstories.util.Ext.setupMapStyle
import com.andresuryana.dicodingstories.util.UiState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : ImagePickerFragment(), ImagePickerFragment.OnImageResultCallback,
    OnMapReadyCallback {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddStoryViewModel>()

    private var storyImage: File? = null

    private lateinit var gMap: GoogleMap
    private var marker: MarkerOptions? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(inflater)

        // Register image picker fragment callback
        registerOnImageResultCallback(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animation
        animateLayout()

        // Get map fragment
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Add observer
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Collect response add story
                viewModel.addStoryState.collectLatest { addStoryStateObserver(it) }
            }
        }

        // Setup image upload container
        setupImageUploadContainer()

        // Setup enable maps switch
        binding.switchMaps.setOnCheckedChangeListener { _, isChecked ->
            binding.mapsContainer.isVisible = isChecked
        }

        // Setup button
        setupButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clear layout binding
        _binding = null
    }

    override fun onImagePickerResult(image: File) {
        storyImage = image
        binding.ivStoryImage.setImageBitmap(BitmapFactory.decodeFile(image.path))
        binding.ivStoryImage.visibility = View.VISIBLE
        binding.uploadHintContainer.visibility = View.GONE
    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map

        // Google maps setting
        gMap.setupMapStyle(requireContext(), R.raw.map_style)
        gMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        // Setup map click listener
        gMap.setOnMapClickListener { position ->
            gMap.clear()
            if (marker == null) marker = MarkerOptions()
            marker?.let {
                it.title(getString(R.string.title_location))
                    .snippet("${position.latitude}, ${position.longitude}")
                    .position(position)
                    .draggable(true)
                gMap.addMarker(it)
            }
        }

        // Enable my location
        getMyLocation()
    }

    private fun getMyLocation() {
        gMap.enableMyLocation(requireActivity(), requestPermissionLauncher)
    }

    private fun animateLayout() {
        // Define animation
        val imageUploadContainer =
            AnimationHelper.slideUpWithFadeIn(binding.imageUploadContainer, 1000L)
        val tilDescription = AnimationHelper.slideUpWithFadeIn(binding.tilDescription, 500L)
        val switchLocation = AnimationHelper.slideUpWithFadeIn(binding.switchMaps, 500L)

        // Start Animation
        AnimatorSet().apply {
            playSequentially(imageUploadContainer, tilDescription, switchLocation)
            start()
        }
    }

    private fun addStoryStateObserver(state: UiState<Boolean>) {
        when (state) {
            is UiState.Success -> {
                hideLoading()
                if (state.data) {
                    showMessage(R.string.success_add_story)
                    lifecycleScope.launch {
                        delay(1000L)
                        getNavController().popBackStack()
                    }
                } else showErrorMessage(R.string.error_add_story)
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

    private fun setupImageUploadContainer() {
        // Click listener
        binding.imageUploadContainer.setOnClickListener {
            // Show image picker dialog (gallery & camera)
            showImageDialogPicker()
        }
    }

    private fun setupButton() {
        // Button back
        binding.buttonBack.setOnClickListener { getNavController().popBackStack() }

        // Button add
        binding.buttonAdd.setOnClickListener {
            validate { image, description, latLng ->
                viewModel.addStory(image, description, latLng?.latitude, latLng?.longitude)
            }
        }
    }

    private fun validate(result: (image: File, description: String, latLng: LatLng?) -> Unit) {
        // Get value
        val description = binding.edAddDescription.text?.trim().toString()

        // Validate
        if (storyImage == null) {
            showErrorMessage(R.string.error_story_image)
            return
        }

        if (description.isEmpty()) {
            showErrorMessage(R.string.error_story_description)
            binding.edAddDescription.requestFocus()
            return
        }

        if (binding.switchMaps.isChecked && binding.mapsContainer.isVisible && marker == null) {
            showErrorMessage(getString(R.string.error_story_location))
            binding.mapsContainer.requestFocus()
            return
        }

        storyImage?.let { image ->
            result(image, description, marker?.position)
        }
    }
}