package com.andresuryana.dicodingstories.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.andresuryana.dicodingstories.R
import com.google.android.gms.maps.GoogleMap
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Ext {

    fun String.isValidEmail(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return this.matches(emailRegex)
    }

    fun Date.formatToRelativeTime(context: Context): String {
        val now = Calendar.getInstance().time
        val diffInMillis = now.time - this.time
        val diffInSeconds = diffInMillis / 1000

        val minuteInMillis = 60
        val hourInMillis = 60 * minuteInMillis
        val dayInMillis = 24 * hourInMillis
        val weekInMillis = 7 * dayInMillis
        val monthInMillis = 30 * dayInMillis
        val yearInMillis = 365 * dayInMillis

        return when {
            diffInSeconds < minuteInMillis -> context.getString(R.string.text_relative_date_now)
            diffInSeconds < hourInMillis -> "${diffInSeconds / minuteInMillis} ${context.getString(R.string.text_relative_date_minutes)}"
            diffInSeconds < dayInMillis -> "${diffInSeconds / hourInMillis} ${context.getString(R.string.text_relative_date_hours)}"
            diffInMillis < weekInMillis -> "${diffInSeconds / dayInMillis} ${context.getString(R.string.text_relative_date_days)}"
            diffInMillis < monthInMillis -> "${diffInSeconds / weekInMillis} ${context.getString(R.string.text_relative_date_weeks)}"
            diffInMillis < yearInMillis -> "${diffInSeconds / monthInMillis} ${context.getString(R.string.text_relative_date_months)}"
            else -> "${diffInSeconds / yearInMillis} ${context.getString(R.string.text_relative_date_years)}"
        }
    }

    fun Date.formatDate(pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(this)
    }

    fun GoogleMap.enableMyLocation(activity: Activity, requestPermissionLauncher: ActivityResultLauncher<String>) {
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            this.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}