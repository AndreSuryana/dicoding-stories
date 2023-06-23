package com.andresuryana.dicodingstories.ui.fragment.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.repository.Repository
import com.andresuryana.dicodingstories.util.Resource
import com.andresuryana.dicodingstories.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _addStoryState = MutableSharedFlow<UiState<Boolean>>()
    val addStoryState = _addStoryState.asSharedFlow()

    private val _refreshTrigger = MutableStateFlow(Unit)
    val refreshTrigger: StateFlow<Unit> = _refreshTrigger

    fun loadStories(): Flow<PagingData<Story>> = runBlocking(Dispatchers.IO) {
        repository.getStories()
    }

    fun addStory(photo: File, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _addStoryState.emit(UiState.Loading)
            when (val result = repository.addNewStory(photo, description)) {
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

    fun refreshStories() {
        _refreshTrigger.value = Unit
    }
}