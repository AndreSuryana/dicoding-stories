package com.andresuryana.dicodingstories.ui.fragment.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.dicodingstories.data.model.User
import com.andresuryana.dicodingstories.data.repository.Repository
import com.andresuryana.dicodingstories.util.Resource
import com.andresuryana.dicodingstories.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<User>>()
    val uiState: LiveData<UiState<User>> = _uiState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            when (val result = repository.register(name, email, password)) {
                is Resource.Success -> {
                    _uiState.value = UiState.Success(User("", name, email, ""))
                }

                is Resource.Error -> {
                    _uiState.value = result.message?.let { UiState.Error(it) }
                }

                is Resource.Failed -> {
                    _uiState.value = result.message.let { UiState.Error(it) }
                }
            }
        }
    }
}