package com.andresuryana.dicodingstories.ui.fragment.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.data.model.User
import com.andresuryana.dicodingstories.databinding.FragmentRegisterBinding
import com.andresuryana.dicodingstories.ui.base.BaseFragment
import com.andresuryana.dicodingstories.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add observer
        viewModel.uiState.observe(viewLifecycleOwner, this::uiStateObserver)

        // Setup button
        setupButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clear layout binding
        _binding = null
    }

    private fun uiStateObserver(state: UiState<User>) {
        when (state) {
            is UiState.Success -> {
                hideLoading()
                showMessage(getString(R.string.success_register_user, state.data.name))
                getNavController().navigate(R.id.loginFragment)
            }

            is UiState.Loading -> {
                showLoading()
            }

            is UiState.Error -> {
                hideLoading()
                showErrorMessage(state.message)
            }
        }
    }

    private fun setupButton() {
        // Button register
        binding.btnRegister.setOnClickListener {
            validate { name, email, password ->
                viewModel.register(name, email, password)
            }
        }

        // Button login
        binding.btnLogin.setOnClickListener {
            getNavController().popBackStack(R.id.loginFragment, true)
        }
    }

    private fun validate(result: (name: String, email: String, password: String) -> Unit) {
        // Get value
        val name = binding.edRegisterName.text?.trim().toString()
        val email = binding.edRegisterEmail.text?.trim().toString()
        val password = binding.edRegisterPassword.text?.trim().toString()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            result(name, email, password)
        }
    }
}