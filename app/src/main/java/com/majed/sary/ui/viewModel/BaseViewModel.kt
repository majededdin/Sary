package com.majed.sary.ui.viewModel

import com.majed.sary.data.repository.BaseRepository

abstract class BaseViewModel<Repo : BaseRepository>(val repo: Repo) :
    BaseValidationVM() {

    internal var currentJob: kotlinx.coroutines.Job? = null

    //------------------------------------------- Settings -----------------------------------------

    override fun onCleared() {
        super.onCleared()
        currentJob = null
    }

    fun clearFirstRun() = repo.clearFirstRun()

    fun clearAllPrefs() = repo.clearAllPrefs()

    fun clearUserData() = repo.clearUserData()

    internal fun setDeviceLanguage(language: String) {
        repo.setDeviceLanguage(language)
    }
}