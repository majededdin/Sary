package com.majed.sary.ui.activities

import am.networkconnectivity.NetworkConnectivity
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.majed.sary.R
import com.majed.sary.data.consts.AppConst
import com.majed.sary.data.local.LocaleHelper
import com.majed.sary.data.model.modified.ErrorHandler
import com.majed.sary.data.remote.Resource
import com.majed.sary.ui.fragments.BaseFragment
import com.majed.sary.ui.fragments.LoadingFragment
import com.majed.sary.ui.viewModel.BaseViewModel
import com.majed.sary.utils.extentions.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseActivity<ViewModelType : BaseViewModel<*>> : AppCompatActivity() {

    private var secondRun = false
    private lateinit var layout: RelativeLayout
    private var viewModel: ViewModelType? = null
    private lateinit var progressLoading: ProgressBar
    private lateinit var layoutNetworkStatus: LinearLayout
    private lateinit var txtNetworkStatus: MaterialTextView
    private val loadingFragment: LoadingFragment = LoadingFragment()

    protected abstract fun getViewModel(): ViewModelType?

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: 1/31/21 still missing for some apis...
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        handleNetworkResponse()
        saveDeviceLanguage()
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    private fun saveDeviceLanguage() {
        AppConst.deviceCurrentLanguage = LocaleHelper.getLanguage(this)
        LocaleHelper.setLocale(this, getDeviceCurrentLanguage())
        viewModel?.setDeviceLanguage(getDeviceCurrentLanguage())
    }

    abstract fun updateView()

    protected abstract fun setErrorHandler(handler: ErrorHandler)

    protected abstract fun viewInit()

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
            finish()
        } else
            supportFragmentManager.popBackStack()
    }

    @SuppressLint("InflateParams")
    override fun setContentView(view: View?) {
        layout = layoutInflater.inflate(R.layout.activity_base, null) as RelativeLayout
        val coordinatorLayout: CoordinatorLayout = layout.findViewById(R.id.containerBase)
        baseInit()

        coordinatorLayout.addView(
            view, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        super.setContentView(layout)
    }

    private fun baseInit() {
        loadingFragment.isCancelable = false
        progressLoading = layout.findViewById(R.id.progress_loading)
        txtNetworkStatus = layout.findViewById(R.id.txt_networkStatus)
        layoutNetworkStatus = layout.findViewById(R.id.layout_networkStatus)
    }

    protected open fun initSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(
            getColorFromRes(getColorAttribute(ColorAttributeEnum.COLOR_PRIMARY)),
            getColorFromRes(getColorAttribute(ColorAttributeEnum.COLOR_PRIMARY_DARK)),
            getColorFromRes(getColorAttribute(ColorAttributeEnum.COLOR_ACCENT))
        )
        swipeRefreshLayout.setOnRefreshListener { updateView() }
    }

    fun setLoadingFragmentStatus(status: Boolean) = showLoadingFragment(status)

    private fun showLoadingFragment(status: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (!status && !loadingFragment.isDetached) loadingFragment.dismiss()
                else if (!loadingFragment.isAdded) loadingFragment.show(
                    supportFragmentManager, javaClass.name
                )
            } catch (ignored: IllegalStateException) {
            }
        }
    }

    open fun getCustomView(): View = findViewById(android.R.id.content)

    open fun getDeviceCurrentLanguage(): String = AppConst.deviceCurrentLanguage

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
    }

    open fun loginIntent() = run {
//        startActivity(Intent(this, AppConst.loginActivity))
    }

    fun changeStatusBarColor(color: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColorFromRes(color)
    }

    fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }

    private fun refreshPageDetails() {
        updateView()
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BaseFragment<*>)
                fragment.updateView()
        }
    }

    fun handleApiResponse(apiResponse: Resource<*>, failureListener: View.OnClickListener) {
        when (apiResponse.status) {
            Resource.Status.OnAuth -> {
                showLoadingFragment(false)
                if (viewModel != null)
                    viewModel!!.clearUserData()
                loginIntent()
            }

            Resource.Status.OnBackEndError -> {
                showLoadingFragment(false)
                apiResponse.message?.let { showToastAsLong(it) }
            }

//            Resource.Status.OnBlocked -> {
//                showLoadingFragment(false)
//                apiResponse.message?.let { showToastAsLong(it) }
//                if (viewModel != null) viewModel!!.clearUserData()
//            }

            Resource.Status.OnError -> {
                showLoadingFragment(false)
//                if (apiResponse.errorBodyAsJSON != null)
//                    getResponseErrors(apiResponse.errorBodyAsJSON!!)
//                else
                apiResponse.message?.let { showToastAsLong(it) }
            }

            Resource.Status.OnFailure -> {
                showLoadingFragment(false)
            }

//            Resource.Status.OnForbidden -> {
//                showLoadingFragment(false)
//                apiResponse.message?.let { showToastAsLong(it) }
//                ("OnForbidden " + apiResponse.message).toLog()
//            }

            Resource.Status.OnHttpException -> {
                showLoadingFragment(false)
                apiResponse.message?.let { showToastAsLong(it) }
            }

            Resource.Status.OnLoading -> showLoadingFragment(true)

            Resource.Status.OnNotFound -> {
                showLoadingFragment(false)
                apiResponse.message?.let { showToastAsLong(it) }
            }

            Resource.Status.OnTimeoutException -> {
                showLoadingFragment(false)
                Snackbar.make(
                    getCustomView(),
                    getString(R.string.request_timeout_please_check_your_connection),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.retry), failureListener).show()
            }

            Resource.Status.OnUnknownHost, Resource.Status.OnConnectException -> {
                showLoadingFragment(false)
                onNetworkConnectionChanged(NetworkConnectivity.NetworkStatus.OnLost)
            }

            Resource.Status.OnSuccess, Resource.Status.IDEL -> showLoadingFragment(false)
        }
    }

    private fun handleNetworkResponse() {
        NetworkConnectivity(this).observe(this, { onNetworkConnectionChanged(it) })
    }

    open fun onNetworkConnectionChanged(status: NetworkConnectivity.NetworkStatus) {
        when (status) {
            NetworkConnectivity.NetworkStatus.OnConnected -> {
                if (secondRun) {
                    secondRun = false
                    txtNetworkStatus.text = getString(R.string.connection_is_back)
                    progressLoading.toGone()
                    layoutNetworkStatus.setBackgroundColor(getColorFromRes(R.color.colorNetworkConnected))

                    refreshPageDetails()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        layoutNetworkStatus.toGone()
                    }
                }
            }

            NetworkConnectivity.NetworkStatus.OnWaiting -> {
                secondRun = true
                txtNetworkStatus.text = getString(R.string.waiting_for_connection)
                progressLoading.toVisible()
                layoutNetworkStatus.toVisible()
                layoutNetworkStatus.setBackgroundColor(getColorFromRes(R.color.colorNetworkWaiting))
            }

            NetworkConnectivity.NetworkStatus.OnLost -> {
                secondRun = true
                txtNetworkStatus.text = getString(R.string.connection_is_lost)
                progressLoading.toGone()
                layoutNetworkStatus.toVisible()
                layoutNetworkStatus.setBackgroundColor(getColorFromRes(R.color.colorNetworkLost))
            }
        }
    }
}