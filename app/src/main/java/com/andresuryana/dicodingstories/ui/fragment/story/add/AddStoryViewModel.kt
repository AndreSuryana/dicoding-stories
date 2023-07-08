package com.andresuryana.dicodingstories.ui.fragment.story.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.dicodingstories.data.repository.Repository
import com.andresuryana.dicodingstories.util.Resource
import com.andresuryana.dicodingstories.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _addStoryState = MutableSharedFlow<UiState<Boolean>>()
    val addStoryState = _addStoryState.asSharedFlow()

    fun addStory(
        photo: File,
        description: String,
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _addStoryState.emit(UiState.Loading)
            when (val result = repository.addNewStory(photo, description, latitude, longitude)) {
                is Resource.Success -> {
                    _addStoryState.emit(UiState.Success(result.data))
                }

                is Resource.Error -> {
                    result.message?.let {
                        _addStoryState.emit(UiState.Error(it))
                    }
                }

                is Resource.Failed -> {
                    _addStoryState.emit(result.message.let { UiState.Error(it) })
                }
            }
        }
    }
}