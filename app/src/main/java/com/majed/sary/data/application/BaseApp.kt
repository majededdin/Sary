package com.majed.sary.data.application

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.majed.sary.BuildConfig
import com.majed.sary.data.consts.AppConst
import com.majed.sary.data.local.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class BaseApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()

    override fun onCreate() {
        super.onCreate()
        instance = this
        initAppConst()
    }

    override fun attachBaseContext(base: Context?) {
        when (Locale.getDefault().language) {
            "ar" -> super.attachBaseContext(LocaleHelper.onAttach(base!!, "ar"))

            "en" -> super.attachBaseContext(LocaleHelper.onAttach(base!!, "en"))

            else -> super.attachBaseContext(LocaleHelper.onAttach(base!!, "en"))
        }
    }

    private fun initAppConst() {
        AppConst.isDebug = BuildConfig.DEBUG
        AppConst.appBaseUrl = BuildConfig.BASE_URL
    }

    companion object {
        var instance: BaseApp = BaseApp()
    }
}