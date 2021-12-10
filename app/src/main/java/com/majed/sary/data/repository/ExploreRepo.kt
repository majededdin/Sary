package com.majed.sary.data.repository

import com.majed.sary.data.dataSource.StoreDataSource
import com.majed.sary.data.local.Prefs
import javax.inject.Inject

class ExploreRepo @Inject constructor(
    private val remoteDataSource: StoreDataSource, private val prefsDataSource: Prefs
) : BaseRepository(prefsDataSource)