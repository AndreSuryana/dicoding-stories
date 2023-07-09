package com.andresuryana.dicodingstories.ui.base

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
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

    private var navController: NavController? = null
    private var loadingDialog: LoadingDialogFragment? = null

    fun getNavController(): NavController {
        return navController ?: run {
            navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
            navController!!
        }
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

    fun showAlertDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes positiveButtonText: Int = R.string.btn_default_positive,
        @StringRes negativeButtonText: Int = R.string.btn_default_negative,
        onPositiveButtonClickListener: () -> Unit,
        isCancelable: Boolean = false
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancelable)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                onPositiveButtonClickListener()
                dialog.dismiss()
            }
            .setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}