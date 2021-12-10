package com.majed.sary.ui.fragments

import am.networkconnectivity.NetworkConnectivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.majed.sary.R
import com.majed.sary.data.model.modified.ErrorHandler
import com.majed.sary.data.remote.Resource
import com.majed.sary.ui.activities.BaseActivity
import com.majed.sary.ui.viewModel.BaseViewModel
import com.majed.sary.utils.extentions.*

abstract class BaseFragment<ViewModelType : BaseViewModel<*>> : Fragment() {

    private lateinit var layout: FrameLayout
    lateinit var baseActivity: BaseActivity<*>
    private var viewModel: ViewModelType? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @LayoutRes
    protected abstract fun setLayout(): Int

    protected abstract fun getViewModel(): ViewModelType?

    abstract fun updateView()

    abstract fun setErrorHandler(handler: ErrorHandler)

    protected abstract fun viewInit()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = getViewModel()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_base, container, false) as FrameLayout
        baseInit()
        initSwipeRefresh(swipeRefreshLayout)
        layoutInflater.inflate(setLayout(), swipeRefreshLayout, true)
        return layout
    }

    private fun baseInit() {
        baseActivity = activity as BaseActivity<*>
        swipeRefreshLayout = layout.findViewById(R.id.containerBaseFragment)
        progressBar = layout.findViewById(R.id.progress_bar)
        progressBar.toGone()
    }

    private fun initSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                baseActivity, requireContext().getColorAttribute(ColorAttributeEnum.COLOR_PRIMARY)
            ),
            ContextCompat.getColor(
                baseActivity,
                requireContext().getColorAttribute(ColorAttributeEnum.COLOR_PRIMARY_DARK)
            ),
            ContextCompat.getColor(
                baseActivity, requireContext().getColorAttribute(ColorAttributeEnum.COLOR_ACCENT)
            )
        )

        swipeRefreshLayout.setOnRefreshListener { updateView() }
    }

    protected fun getSwipeRefresh() = swipeRefreshLayout

    protected fun setOnSwipeRefreshStatus(status: Boolean) {
        swipeRefreshLayout.isEnabled = status
    }

    open fun getCustomView() = layout

    open fun getDeviceCurrentLanguage(): String = baseActivity.getDeviceCurrentLanguage()

    fun setLoadingStatus(status: Boolean) {
        showLoading(status)
    }

    private fun showLoading(status: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                showProgressBar(status)

            } catch (ignored: IllegalStateException) {
            }
        }
    }

    private fun showProgressBar(status: Boolean) {
        if (status)
            progressBar.toVisible()
        else
            progressBar.toGone()
    }

    open fun loginIntent() = baseActivity.loginIntent()

    open fun handleApiResponse(apiResponse: Resource<*>, failureListener: View.OnClickListener) {
        when (apiResponse.status) {
            Resource.Status.OnAuth -> {
                showProgressBar(false)
                if (viewModel != null)
                    viewModel!!.clearUserData()
                loginIntent()
            }

            Resource.Status.OnBackEndError -> {
                showProgressBar(false)
                apiResponse.message?.let { baseActivity.showToastAsLong(it) }
            }

//            Resource.Status.OnBlocked -> {
//                showProgressBar(false)
//                apiResponse.message?.let { baseActivity.showToastAsLong(it) }
//                if (viewModel != null) viewModel!!.clearUserData()
//            }

            Resource.Status.OnError -> {
                showProgressBar(false)
//                if (apiResponse.errorBodyAsJSON != null)
//                    getResponseErrors(apiResponse.errorBodyAsJSON!!)
//                else
                apiResponse.message?.let { baseActivity.showToastAsLong(it) }
            }

            Resource.Status.OnFailure -> {
                showProgressBar(false)
            }

//            Resource.Status.OnForbidden -> {
//                showProgressBar(false)
//                apiResponse.message?.let { baseActivity.showToastAsLong(it) }
//                ("OnForbidden " + apiResponse.message).toLog()
//            }

            Resource.Status.OnHttpException -> {
                showProgressBar(false)
                apiResponse.message?.let { baseActivity.showToastAsLong(it) }
            }

            Resource.Status.OnLoading -> showProgressBar(true)

            Resource.Status.OnNotFound -> {
                showProgressBar(false)
                apiResponse.message?.let { baseActivity.showToastAsLong(it) }
            }

            Resource.Status.OnTimeoutException -> {
                showProgressBar(false)
                Snackbar.make(
                    getCustomView(),
                    getString(R.string.request_timeout_please_check_your_connection),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.retry), failureListener).show()
            }

            Resource.Status.OnUnknownHost, Resource.Status.OnConnectException -> {
                showProgressBar(false)
                baseActivity.onNetworkConnectionChanged(NetworkConnectivity.NetworkStatus.OnLost)
            }

            Resource.Status.OnSuccess, Resource.Status.IDEL -> showProgressBar(false)
        }
    }

}