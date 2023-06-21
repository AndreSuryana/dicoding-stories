package com.andresuryana.dicodingstories.ui.fragment.auth.login

import androidx.lifecycle.ViewModel
import com.andresuryana.dicodingstories.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // TODO: Implement login view model
}