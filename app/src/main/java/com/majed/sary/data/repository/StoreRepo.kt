package com.majed.sary.data.repository

import com.majed.sary.data.dataSource.StoreDataSource
import com.majed.sary.data.local.Prefs
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepo @Inject constructor(
    private val remoteDataSource: StoreDataSource, private val prefsDataSource: Prefs
) : BaseRepository(prefsDataSource) {

    suspend fun getBanners() = flow {
        val bannerCallResult = remoteDataSource.getBanners(getHeaderMap())
        emit(bannerCallResult)
    }

    suspend fun getCatalogs() = flow {
        val catalogCallResult = remoteDataSource.getCatalog(getHeaderMap())
        emit(catalogCallResult)
    }
}