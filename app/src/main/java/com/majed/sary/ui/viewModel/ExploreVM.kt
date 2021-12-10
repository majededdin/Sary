package com.majed.sary.ui.viewModel

import com.majed.sary.data.repository.ExploreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreVM @Inject constructor(repo: ExploreRepo) : BaseViewModel<ExploreRepo>(repo)