package com.majed.sary.data.dataSource

import com.majed.sary.data.remote.ApiService
import javax.inject.Inject

class ExploreDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource()