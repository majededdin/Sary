package com.majed.sary.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException

class Prefs(private val context: Context) {

    companion object {
        private val Context.prefs: DataStore<Preferences> by preferencesDataStore(name = "UserPrefs")
        private val APP_LANGUAGE = stringPreferencesKey("appLanguage")
        private val DEVICE_LANGUAGE = stringPreferencesKey("deviceLanguage")
        private val APP_TOKEN = stringPreferencesKey("appToken")
        private val FIRST_RUN = booleanPreferencesKey("firstRun")
    }

    suspend fun clearAllPrefs() {
        clearFirstRun()
        clearUserData()
    }

    suspend fun clearUserData() {
        clearToken()
    }

    //--------------------------------------------- Device Language --------------------------------

    val deviceLanguage: Flow<String> = context.prefs.data.catch { handleEmptyError(it) }
        .map { it[DEVICE_LANGUAGE] ?: "en" }

    suspend fun setDeviceLanguage(language: String) =
        context.prefs.edit { it[DEVICE_LANGUAGE] = language }

    //--------------------------------------------- Token ------------------------------------------

    val bearerToken: Flow<String> = context.prefs.data.catch { handleEmptyError(it) }
        .map { "Bearer ".plus(it[APP_TOKEN]) }

    val token: Flow<String?> = context.prefs.data.catch { handleEmptyError(it) }
        .map { it[APP_TOKEN] }

    suspend fun setToken(token: String) = context.prefs.edit { it[APP_TOKEN] = token }

    val isTokenExist: Flow<Boolean> = context.prefs.data.catch { handleBooleanError(it) }
        .map { !it[APP_TOKEN].isNullOrEmpty() }

    private suspend fun clearToken() = context.prefs.edit { it[APP_TOKEN] = "" }

    //--------------------------------------------- FirstRun ---------------------------------------

    val isFirstRun: Flow<Boolean> = context.prefs.data.catch { handleBooleanError(it) }
        .map { it[FIRST_RUN] ?: true }

    suspend fun clearFirstRun() = context.prefs.edit { it[FIRST_RUN] = false }

    //--------------------------------------------- User -------------------------------------------

//    suspend fun setUserAddress(address: Address) {
//        context.prefs.edit {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.address = address
//                it[USER] = user.toJSON()
//            }
//        }
//    }
//
//    suspend fun setUserPic(media: Media) {
//        context.prefs.edit {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.photo = media
//                it[USER] = user.toJSON()
//            }
//        }
//    }
//
//    suspend fun clearUserPic() {
//        context.prefs.edit {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.photo = null
//                it[USER] = user.toJSON()
//            }
//        }
//    }
//
//    suspend fun setEmailVerified() {
//        context.prefs.edit {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.email_verified = true
//                it[USER] = user.toJSON()
//            }
//        }
//    }
//
//    suspend fun setPhoneVerified() {
//        context.prefs.edit {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.phone_verified = true
//                it[USER] = user.toJSON()
//            }
//        }
//    }
//
//    suspend fun setUserBlocked(isBlocked: Boolean) {
//        context.prefs.edit {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.blocked = isBlocked
//                it[USER] = user.toJSON()
//            }
//        }
//    }
//
//    val isEmailVerified: Flow<Boolean> = context.prefs.data.catch { handleBooleanError(it) }
//        .map {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.email_verified!!
//            } else false
//        }
//
//    val isPhoneVerified: Flow<Boolean> = context.prefs.data.catch { handleBooleanError(it) }
//        .map {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.phone_verified!!
//            } else false
//        }
//
//    val isUserBlocked: Flow<Boolean> = context.prefs.data.catch { handleBooleanError(it) }
//        .map {
//            if (!it[USER].isNullOrEmpty()) {
//                val user = getUserFromJSON(it[USER]!!)
//                user.blocked
//            } else false
//        }

    //--------------------------------------------- JSON Formatter ---------------------------------

//    private fun getUserFromJSON(model: String): User =
//        Gson().fromJson(model, object : TypeToken<User>() {}.type)

    //--------------------------------------------- PrefsErrors ------------------------------------

    private fun handleEmptyError(it: Throwable) = flow {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else
            throw it
    }

    private fun handleBooleanError(it: Throwable) = flow {
        if (it is IOException) {
            it.printStackTrace()
            emit(false)
        } else
            throw it
    }

}