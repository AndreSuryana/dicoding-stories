package com.andresuryana.dicodingstories.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Ext {

    fun String.isValidEmail(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return this.matches(emailRegex)
    }

    fun Date.formatToRelativeTime(): String {
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
            diffInSeconds < minuteInMillis -> "Just now"
            diffInSeconds < hourInMillis -> "${diffInSeconds / minuteInMillis} minutes ago"
            diffInSeconds < dayInMillis -> "${diffInSeconds / hourInMillis} hours ago"
            diffInMillis < weekInMillis -> "${diffInSeconds / dayInMillis} days ago"
            diffInMillis < monthInMillis -> "${diffInSeconds / weekInMillis} weeks ago"
            diffInMillis < yearInMillis -> "${diffInSeconds / monthInMillis} months ago"
            else -> "${diffInSeconds / yearInMillis} years ago"
        }
    }

    fun Date.formatDate(pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(this)
    }
}