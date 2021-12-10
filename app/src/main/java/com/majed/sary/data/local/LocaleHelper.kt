package com.majed.sary.data.local

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import java.util.*

class LocaleHelper {
    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

        fun onAttach(context: Context): Context {
            val language = getPersistedData(context, Locale.getDefault().language)
            return setLocale(context, language)
        }

        fun onAttach(context: Context, defaultLanguage: String): Context {
            val language = getPersistedData(context, defaultLanguage)
            return setLocale(context, language)
        }

        fun getLanguage(context: Context): String {
            return getPersistedData(context, Locale.getDefault().language)
        }

        fun setLocale(context: Context, language: String): Context {
            persist(context, language)
            return updateResourcesUp(context, language)
        }

        private fun getPersistedData(context: Context, defaultLanguage: String): String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(SELECTED_LANGUAGE, defaultLanguage)!!
        }

        private fun persist(context: Context, language: String) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = prefs.edit()

            editor.putString(SELECTED_LANGUAGE, language)
            editor.apply()
        }

        @SuppressWarnings("deprecation")
        private fun updateResourcesUp(context: Context, language: String): Context {
            var newContext: Context = context
            val activityRes = newContext.resources
            val activityConf = activityRes.configuration
            val newLocale = Locale(language)
            activityConf.setLocale(newLocale)
            activityConf.setLayoutDirection(newLocale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
                newContext = newContext.createConfigurationContext(activityConf)
            else
                activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)
            return newContext
        }
    }
}