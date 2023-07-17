package com.panassevich.panassevich.feature.loans.settings.presentation

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import java.util.Locale

class AppLanguageManager(private val context: Context) {

    companion object {
        private const val KEY_SELECTED_LANGUAGE = "selected_language"

        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_RUSSIAN = "ru"
    }

    fun getCurrentLanguage(): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(KEY_SELECTED_LANGUAGE, null) ?: Locale.getDefault().language
    }

    fun setCurrentLanguage(language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putString(KEY_SELECTED_LANGUAGE, language).apply()
    }

    fun getContextForSelectedLanguage(): Context {
        val locale = Locale(getCurrentLanguage())
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return this.context.createConfigurationContext(configuration)
    }
}