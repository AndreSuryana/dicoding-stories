package com.andresuryana.dicodingstories.ui.fragment.auth.login

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
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<User>>()
    val uiState: LiveData<UiState<User>> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.postValue(UiState.Loading)
            when (val result = repository.login(email, password)) {
                is Resource.Success -> {
                    _uiState.postValue(UiState.Success(result.data))
                }

                is Resource.Error -> {
                    _uiState.postValue(result.message?.let { UiState.Error(it) })
                }

                is Resource.Failed -> {
                    _uiState.postValue(result.message.let { UiState.Error(it) })
                }
            }
        }
    }
}