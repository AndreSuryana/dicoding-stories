package com.andresuryana.dicodingstories.ui.fragment.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.data.model.User
import com.andresuryana.dicodingstories.databinding.FragmentLoginBinding
import com.andresuryana.dicodingstories.ui.base.BaseFragment
import com.andresuryana.dicodingstories.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clear layout binding
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add observer
        viewModel.uiState.observe(viewLifecycleOwner, this::uiStateObserver)

        // Setup button
        setupButton()
    }

    private fun uiStateObserver(state: UiState<User>) {
        when (state) {
            is UiState.Success -> {
                hideLoading()
                showMessage(getString(R.string.success_register_user, state.data.name))
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
        // Button login
        binding.btnLogin.setOnClickListener {
            validate { email, password ->
                viewModel.login(email, password)
            }
        }

        // Button register
        binding.btnRegister.setOnClickListener {
            getNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun validate(result: (email: String, password: String) -> Unit) {
        // Get value
        val email = binding.edLoginEmail.text?.trim().toString()
        val password = binding.edLoginPassword.text?.trim().toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            result(email, password)
        }
    }
}