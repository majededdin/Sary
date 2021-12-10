package com.majed.sary.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.majed.sary.data.model.service.Banner
import com.majed.sary.data.model.service.Catalog
import com.majed.sary.data.remote.Resource
import com.majed.sary.data.repository.StoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreVM @Inject constructor(repo: StoreRepo) : BaseViewModel<StoreRepo>(repo) {

    private val _bannersResult = MutableLiveData<Resource<Banner>>()
    val bannersResult: LiveData<Resource<Banner>> = _bannersResult

    private val _catalogsResult = MutableLiveData<Resource<Catalog>>()
    val catalogsResult: LiveData<Resource<Catalog>> = _catalogsResult

    fun getStoreInfo() {
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            repo.getBanners().collect { _bannersResult.postValue(it) }
            repo.getCatalogs().collect { _catalogsResult.postValue(it) }
        }
    }
}