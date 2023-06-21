package com.andresuryana.dicodingstories.ui.base

import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.ui.component.dialog.LoadingDialogFragment
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {

    private var loadingDialog: LoadingDialogFragment

    init {
        loadingDialog = LoadingDialogFragment()
    }

    private fun showLoading() {
        loadingDialog = LoadingDialogFragment()
        loadingDialog.show(childFragmentManager, LoadingDialogFragment::class.simpleName)
    }

    private fun hideLoading() {
        loadingDialog.dismissAllowingStateLoss()
    }

    private fun showMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    private fun showMessage(@StringRes message: Int) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    private fun showErrorMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).apply {
                setBackgroundTint(ContextCompat.getColor(it.context, R.color.error))
                show()
            }
        }
    }

    private fun showErrorMessage(@StringRes message: Int) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).apply {
                setBackgroundTint(ContextCompat.getColor(it.context, R.color.error))
                show()
            }
        }
    }
}