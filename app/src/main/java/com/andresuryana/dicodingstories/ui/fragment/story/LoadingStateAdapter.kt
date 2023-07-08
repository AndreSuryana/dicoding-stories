package com.andresuryana.dicodingstories.ui.fragment.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andresuryana.dicodingstories.databinding.ItemStoryLoadingBinding

class LoadingStateAdapter(
    private val onRetryClickListener: () -> Unit
) : LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            ItemStoryLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onRetryClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    class ViewHolder(
        private val binding: ItemStoryLoadingBinding,
        onRetryClickListener: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                onRetryClickListener.invoke()
            }
        }

        fun onBind(state: LoadState) {
            if (state is LoadState.Error) {
                binding.tvMessage.text = state.error.localizedMessage
            }
            binding.progressBar.isVisible = state is LoadState.Loading
            binding.btnRetry.isVisible = state is LoadState.Error
            binding.tvMessage.isVisible = state is LoadState.Error
        }
    }
}