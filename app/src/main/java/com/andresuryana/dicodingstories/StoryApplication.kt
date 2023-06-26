package com.andresuryana.dicodingstories

import android.app.Application
import android.content.Context
import com.andresuryana.dicodingstories.util.LocaleHelper
import com.andresuryana.dicodingstories.util.SettingHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StoryApplication : Application() {

    override fun attachBaseContext(base: Context) {
        // Get language
        val languageCode = SettingHelper(base).getLanguage()

        // Apply language config
        LocaleHelper.setLocale(base, languageCode)

        super.attachBaseContext(base)
    }
}