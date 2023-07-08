package com.andresuryana.dicodingstories.ui.fragment.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.repository.Repository
import com.andresuryana.dicodingstories.rule.MainDispatcherRule
import com.andresuryana.dicodingstories.util.DummyData
import com.andresuryana.dicodingstories.util.FlowTestUtil.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: Repository

    @Test
    fun `when request stories should not null`() = runTest {
        // Generate expected value
        val dummyStories = DummyData.generateDummyStories()
        val pagingData = StoryPagingSource.snapshot(dummyStories)
        val expectedData: Flow<PagingData<Story>> = flowOf(pagingData)

        // Tell mockito to return the expected value
        Mockito.`when`(repository.getStories()).thenReturn(expectedData)

        // Call method
        val viewModel = StoryViewModel(repository)
        val actualData = viewModel.loadStories().getOrAwaitValue()

        // Assign paging data into adapter
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = updateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualData)

        // Assertion
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when request stories return empty`() = runTest {
        // Generate expected value
        val pagingData = StoryPagingSource.snapshot(emptyList())
        val expectedData: Flow<PagingData<Story>> = flowOf(pagingData)

        // Tell mockito to return the expected value
        Mockito.`when`(repository.getStories()).thenReturn(expectedData)

        // Call method
        val viewModel = StoryViewModel(repository)
        val actualData = viewModel.loadStories().getOrAwaitValue()

        // Assign paging data into adapter
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = updateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualData)

        // Assertion
        Assert.assertEquals(0, differ.snapshot().size)
    }

    private val updateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}