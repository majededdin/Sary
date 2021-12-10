package com.majed.sary.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers

interface ApiService {

    @GET("v2.5.1/baskets/76097/banners")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getBanners(@HeaderMap map: HashMap<String, Any>): ResponseBody

    @GET("baskets/76097/catalog")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getCatalogs(@HeaderMap map: HashMap<String, Any>): ResponseBody
}