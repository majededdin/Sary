package com.majed.sary.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.majed.sary.data.consts.AppConst
import com.majed.sary.data.local.Prefs
import com.majed.sary.data.remote.ApiService
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCache(): Cache =
        Cache(File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()), 1024)

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (AppConst.isDebug) HttpLoggingInterceptor.Level.BASIC
        else
            HttpLoggingInterceptor.Level.NONE

        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        cache: Cache, interceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .cache(cache)
        .retryOnConnectionFailure(true)
        .connectionPool(ConnectionPool(15, 500000, TimeUnit.MILLISECONDS))
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(interceptor)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient.Builder, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(AppConst.appBaseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providePrefs(@ApplicationContext appContext: Context) = Prefs(appContext)
}