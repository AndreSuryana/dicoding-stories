package com.andresuryana.dicodingstories.ui.fragment.story

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _refreshTrigger = MutableStateFlow(Unit)
    val refreshTrigger: StateFlow<Unit> = _refreshTrigger

    fun loadStories(): Flow<PagingData<Story>> = runBlocking(Dispatchers.IO) {
        repository.getStories()
    }

    fun refreshStories() {
        _refreshTrigger.value = Unit
    }
}