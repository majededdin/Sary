package com.majed.sary.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.majed.sary.R
import com.majed.sary.data.model.modified.ErrorHandler
import com.majed.sary.data.model.service.Banner
import com.majed.sary.data.model.service.Catalog
import com.majed.sary.data.remote.Resource
import com.majed.sary.databinding.FragmentStoreBinding
import com.majed.sary.ui.adapters.MediaPagerAdapter
import com.majed.sary.ui.adapters.StoreDynamicAdapter
import com.majed.sary.ui.viewModel.StoreVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment : BaseFragment<StoreVM>() {

    private val storeVM: StoreVM by viewModels()
    private lateinit var adapterBanners: MediaPagerAdapter
    private lateinit var adapterDynamic: StoreDynamicAdapter

    private val binding get() = _binding!!
    private var _binding: FragmentStoreBinding? = null

    override fun setLayout(): Int = R.layout.fragment_store

    override fun getViewModel(): StoreVM = storeVM

    override fun onDestroyView() {
        super.onDestroyView()
        // calling this to avoid memory leak.
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStoreBinding.bind(view)
        setOnSwipeRefreshStatus(false)
        viewInit()
        updateView()

        storeVM.bannersResult.observe(viewLifecycleOwner, this::bannersResult)
        storeVM.catalogsResult.observe(viewLifecycleOwner, this::catalogsResult)
    }

    private fun catalogsResult(apiResponse: Resource<Catalog>) {
        getSwipeRefresh().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.status == Resource.Status.OnSuccess) {
            adapterDynamic.setItems(apiResponse.listOfModel)
        }
    }

    private fun bannersResult(apiResponse: Resource<Banner>) {
        getSwipeRefresh().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.status == Resource.Status.OnSuccess) {
            adapterBanners.setItems(apiResponse.listOfModel)
            binding.viewpagerBanners.adapter = adapterBanners
        }
    }

    override fun updateView() {
        storeVM.getStoreInfo()
    }

    override fun setErrorHandler(handler: ErrorHandler) {
    }

    override fun viewInit() {
        adapterBanners = MediaPagerAdapter(baseActivity)
        binding.viewpagerBanners.adapter = adapterBanners
        TabLayoutMediator(binding.tablayoutBanners, binding.viewpagerBanners)
        { _: TabLayout.Tab?, _: Int -> }.attach()

        if (getDeviceCurrentLanguage() == "ar") {
            binding.viewpagerBanners.layoutDirection = View.LAYOUT_DIRECTION_RTL
            binding.tablayoutBanners.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            binding.viewpagerBanners.layoutDirection = View.LAYOUT_DIRECTION_LTR
            binding.tablayoutBanners.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }

        adapterDynamic = StoreDynamicAdapter(baseActivity)
        binding.recyclerStore.adapter = adapterDynamic
        binding.recyclerStore.layoutManager = LinearLayoutManager(requireContext())
    }
}