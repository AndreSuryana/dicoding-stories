package com.andresuryana.dicodingstories.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.ui.component.dialog.LoadingDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    private lateinit var navController: NavController
    private var loadingDialog: LoadingDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init nav controller
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    fun getNavController(): NavController {
        return navController
    }

    fun showLoading() {
        if (loadingDialog == null || loadingDialog?.isAdded == false) {
            loadingDialog = LoadingDialogFragment()
            loadingDialog?.show(parentFragmentManager, javaClass.simpleName)
        }
    }

    fun hideLoading() {
        if (loadingDialog != null && loadingDialog?.isAdded == true) {
            loadingDialog?.dismissAllowingStateLoss()
        }
    }

    fun showMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    fun showMessage(@StringRes message: Int) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    fun showErrorMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).apply {
                setBackgroundTint(ContextCompat.getColor(it.context, R.color.error))
                show()
            }
        }
    }

    fun showErrorMessage(@StringRes message: Int) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).apply {
                setBackgroundTint(ContextCompat.getColor(it.context, R.color.error))
                show()
            }
        }
    }
}