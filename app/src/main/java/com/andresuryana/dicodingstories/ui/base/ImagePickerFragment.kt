package com.andresuryana.dicodingstories.ui.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.util.FileHelper.createImageTempFile
import com.andresuryana.dicodingstories.util.FileHelper.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

private const val AUTHORITY_NAME = "com.andresuryana.dicodingstories.camera"

@AndroidEntryPoint
open class ImagePickerFragment : BaseFragment() {

    private lateinit var selectedAction: Action
    private lateinit var imagePath: String

    private lateinit var callback: OnImageResultCallback

    // Launcher
    private val launcherImageIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                when (selectedAction) {
                    Action.CAMERA -> {
                        val image = File(imagePath)
                        callback.onImagePickerResult(image)
                    }

                    Action.GALLERY -> {
                        val imageUri = it.data?.data as Uri
                        imageUri.let { uri ->
                            val image = uriToFile(uri, requireActivity())
                            callback.onImagePickerResult(image)
                        }
                    }

                    else -> Unit
                }
            }
        }

    private val launcherPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, start photo capture
                startCameraCapture()
            } else {
                // Permission denied, handle accordingly
                val permissionName = getString(
                    if (selectedAction == Action.CAMERA) R.string.title_camera
                    else R.string.title_storage
                )
                showErrorMessage(getString(R.string.error_permission_denied, permissionName))
            }
        }

    fun registerOnImageResultCallback(callback: OnImageResultCallback) {
        this.callback = callback
    }

    /* Dialog */
    fun showImageDialogPicker() {
        val actions = Action.values().map { requireActivity().getString(it.title) }.toTypedArray()
        AlertDialog.Builder(requireActivity())
            .setTitle(requireActivity().getString(R.string.title_select_image))
            .setItems(actions) { dialog, item ->
                when (Action.values()[item]) {
                    Action.CAMERA -> { // Camera
                        selectedAction = Action.CAMERA
                        startCameraCapture()
                    }

                    Action.GALLERY -> { // Gallery
                        selectedAction = Action.GALLERY
                        startGalleryPick()
                    }

                    Action.CANCEL -> { // Cancel
                        dialog.dismiss()
                    }
                }
            }
            .show()
    }

    /* Camera */
    private fun startCameraCapture() {
        // Perform permission checks
        if (checkCameraPermission()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(requireActivity().packageManager)

            createImageTempFile(requireActivity().application).also { file ->
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireActivity(),
                    AUTHORITY_NAME,
                    file
                )
                imagePath = file.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherImageIntent.launch(intent)
            }
        }
    }

    /* Gallery */
    private fun startGalleryPick() {
        // Check for storage permission
        if (checkStoragePermission()) {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val intentChooser = Intent.createChooser(intent, "Choose a Picture")
            launcherImageIntent.launch(intentChooser)
        }
    }

    /* Permission Check */
    private fun checkCameraPermission(): Boolean {
        // Check for camera permission
        return if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request camera permission
            launcherPermission.launch(Manifest.permission.CAMERA)
            false
        } else {
            // Camera permission already granted
            true
        }
    }

    private fun checkStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request storage permission
                launcherPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                false
            } else {
                // Storage permission already granted
                true
            }
        }
        return true
    }

    /* Callback Interface */
    interface OnImageResultCallback {
        fun onImagePickerResult(image: File)
    }

    enum class Action(@StringRes val title: Int) {
        CAMERA(R.string.title_camera),
        GALLERY(R.string.title_gallery),
        CANCEL(R.string.btn_cancel)
    }
}