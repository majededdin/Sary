package com.majed.sary.ui.activities

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.majed.sary.R
import com.majed.sary.data.model.modified.ErrorHandler
import com.majed.sary.databinding.ActivityExploreBinding
import com.majed.sary.ui.fragments.StoreFragment
import com.majed.sary.ui.viewModel.ExploreVM
import com.majed.sary.utils.extentions.loadWithFade
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreActivity : BaseActivity<ExploreVM>(), NavigationBarView.OnItemSelectedListener {

    private val exploreVM: ExploreVM by viewModels()
    private lateinit var binding: ActivityExploreBinding
    private lateinit var storeFragment: StoreFragment

    override fun getViewModel(): ExploreVM = exploreVM

    override fun onBackPressed() {
        for (fragment: Fragment in supportFragmentManager.fragments) {
            if (fragment is StoreFragment) {
                setResult(Activity.RESULT_OK)
                super.onBackPressed()
            } else {
                loadFragment(0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewInit()
        loadFragment(storeFragment)
        loadFragment(0)
    }

    private fun loadFragment(position: Int) = when (position) {
        0 -> {
            binding.bottomNavigationView.getChildAt(0)
            binding.bottomNavigationView.selectedItemId = R.id.nav_store
        }

        2 -> {
            binding.bottomNavigationView.getChildAt(2)
            binding.bottomNavigationView.selectedItemId = R.id.nav_profile
        }

        else -> {
            binding.bottomNavigationView.getChildAt(1)
            binding.bottomNavigationView.selectedItemId = R.id.nav_orders
        }
    }

    override fun updateView() {
    }

    override fun setErrorHandler(handler: ErrorHandler) {
    }

    override fun viewInit() {
        storeFragment = StoreFragment()
        binding.bottomNavigationView.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = loadFragment(
        when (item.itemId) {
            R.id.nav_store -> {
                binding.baseToolbar.title = getString(R.string.app_name)
                storeFragment
            }
            R.id.nav_orders -> {
                binding.baseToolbar.title = getString(R.string.app_name)
                StoreFragment()
            }
            else -> {
                binding.baseToolbar.title = getString(R.string.app_name)
                StoreFragment()
            }
        }
    )

    private fun loadFragment(fragment: Fragment): Boolean {
        loadWithFade(fragment, R.id.homeFrameLayout)
        return true
    }
}