package com.andresuryana.dicodingstories.ui.fragment.story.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.repository.Repository
import com.andresuryana.dicodingstories.util.Resource
import com.andresuryana.dicodingstories.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryMapViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Story>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getStoriesWithLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(UiState.Loading)
            when (val result = repository.getStoriesWithLocation()) {
                is Resource.Success -> {
                    _uiState.emit(UiState.Success(result.data))
                }

                is Resource.Error -> {
                    result.message?.let {
                        _uiState.emit(UiState.Error(it))
                    }
                }

                is Resource.Failed -> {
                    _uiState.emit(result.message.let { UiState.Error(it) })
                }
            }
        }
    }
}