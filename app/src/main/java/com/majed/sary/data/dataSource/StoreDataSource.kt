package com.majed.sary.data.dataSource

import com.google.gson.reflect.TypeToken
import com.majed.sary.data.consts.Params
import com.majed.sary.data.model.service.Banner
import com.majed.sary.data.model.service.Catalog
import com.majed.sary.data.remote.ApiService
import javax.inject.Inject

class StoreDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource() {

    suspend fun getBanners(headerMap: HashMap<String, Any>) = getApiResult<Banner>(
        { apiService.getBanners(headerMap) },
        object : TypeToken<List<Banner>>() {}.type, Params.RESULT
    )

    suspend fun getCatalog(headerMap: HashMap<String, Any>) = getApiResult<Catalog>(
        { apiService.getCatalogs(headerMap) },
        object : TypeToken<List<Catalog>>() {}.type, Params.RESULT
    )
}