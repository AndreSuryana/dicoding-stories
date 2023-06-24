package com.andresuryana.dicodingstories.ui.fragment.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.databinding.FragmentStoryBinding
import com.andresuryana.dicodingstories.ui.base.BaseFragment
import com.andresuryana.dicodingstories.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoryFragment : BaseFragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StoryViewModel>()

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clear layout binding
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapter
        storyAdapter = StoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = storyAdapter
        }

        // Add observer
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.refreshTrigger.collectLatest {
                    // Collect stories again if refresh trigger is updated
                    viewModel.loadStories().collectLatest { storyAdapter.submitData(it) }
                }

                // Collect response add story
                viewModel.addStoryState.collectLatest { addStoryStateObserver(it) }
            }
        }

        // Setup refresh layout
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refreshStories()
            binding.refreshLayout.isRefreshing = false
        }

        // Setup button
        setupButton()
    }

    private fun addStoryStateObserver(state: UiState<Boolean>) {
        when (state) {
            is UiState.Success -> {
                hideLoading()
                if (state.data) showMessage(R.string.success_add_story)
                else showErrorMessage(R.string.error_add_story)
            }

            is UiState.Error -> {
                hideLoading()
                showErrorMessage(state.message)
            }

            is UiState.Loading -> {
                showLoading()
            }
        }
    }

    private fun setupButton() {
        // Button add story
        binding.fabAddStory.setOnClickListener {
            showAddStoryDialog()
        }

        // Button logout
        binding.actionSetting.setOnClickListener {
            // Navigate to setting fragment
            getNavController().navigate(R.id.action_storyFragment_to_settingFragment)
        }

        // Button setting
        binding.actionLogout.setOnClickListener {
            // Show alert dialog for logout
            showAlertDialog(
                title = R.string.dialog_title_logout,
                message = R.string.dialog_message_logout,
                positiveButtonText = R.string.btn_logout,
                onPositiveButtonClickListener = {
                    // Clear session and navigate to loginFragment
                    val session = SessionHelperImpl(requireContext())
                    session.clearSession()
                    getNavController().navigate(R.id.action_global_loginFragment)
                }
            )
        }
    }
}