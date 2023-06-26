package com.andresuryana.dicodingstories.ui.fragment.setting

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.util.SettingHelper

class SettingFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Set preference resources
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)

        // Setup preference listener
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove preference listener
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val settingHelper = SettingHelper(requireContext())
        Toast.makeText(requireContext(), "$key=${sharedPreferences?.getString(key, null)}", Toast.LENGTH_SHORT).show()
        when (key) {
            getString(R.string.settings_key_language_preference) -> {
                // Language
                val language = sharedPreferences?.getString(key, "en") ?: "en"
                settingHelper.setLanguage(language)

                // Update ui
                requireActivity().recreate()
            }
            getString(R.string.settings_key_dark_mode_preference) -> {
                // Dark Mode
                val isDarkMode = sharedPreferences?.getBoolean(key, false) ?: false
                settingHelper.setDarkMode(isDarkMode)

                // TODO: Update dark mode here!
            }
            else -> Unit
        }
    }
}