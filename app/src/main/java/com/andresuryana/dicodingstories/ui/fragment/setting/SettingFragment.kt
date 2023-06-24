package com.andresuryana.dicodingstories.ui.fragment.setting

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.andresuryana.dicodingstories.R

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
        Toast.makeText(requireContext(), "$key=${sharedPreferences?.getString(key, null)}", Toast.LENGTH_SHORT).show()
        when (key) {
            // TODO: Action according to the key!
            else -> Unit
        }
    }

    private fun updatePreference(key: String) {
        // TODO: Update value
    }
}