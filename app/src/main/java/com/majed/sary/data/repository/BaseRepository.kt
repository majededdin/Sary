package com.majed.sary.data.repository

import com.majed.sary.data.consts.Params
import com.majed.sary.data.local.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseRepository(private val prefs: Prefs) {

    //---------------------------------------- Header Map ------------------------------------------

    protected suspend fun getHeaderMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.DEVICE_TYPE] = "android"
        map[Params.APP_VERSION] = "3.1.1.0.0"
        map[Params.X_LOCALE] = getDeviceLanguage()
        map[Params.AUTH_PARAM] =
            "token eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODg2NiwidXNlcl9waG9uZSI6Ijk2NjU2NDk4OTI1MCJ9.VYE28vtnMRLmwBISgvvnhOmPuGueW49ogOhXm5ZqsGU"
        return map
    }

    //---------------------------------------- Prefs Methods ---------------------------------------

    internal suspend fun getDeviceLanguage(): String = prefs.deviceLanguage.first()

    protected suspend fun isFirstRun(): Boolean = prefs.isFirstRun.first()

    private suspend fun getBearerToken(): String = prefs.bearerToken.first()

    internal suspend fun isTokenExist(): Boolean = prefs.isTokenExist.first()

    internal fun clearFirstRun() = CoroutineScope(Dispatchers.IO).launch { prefs.clearFirstRun() }

    internal fun clearAllPrefs() = CoroutineScope(Dispatchers.IO).launch { prefs.clearAllPrefs() }

    internal fun clearUserData() = CoroutineScope(Dispatchers.IO).launch { prefs.clearUserData() }

    internal fun setDeviceLanguage(language: String) =
        CoroutineScope(Dispatchers.IO).launch { prefs.setDeviceLanguage(language) }
}