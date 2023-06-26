package com.andresuryana.dicodingstories.ui.fragment.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.databinding.ItemStoryBinding
import com.andresuryana.dicodingstories.util.Ext.formatToRelativeTime
import com.bumptech.glide.Glide

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((story: Story) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) holder.onBind(story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setOnItemClickListener(onItemClickListener: (story: Story) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(story: Story) {
            // Load image
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(
                    CircularProgressDrawable(itemView.context).apply {
                        strokeWidth = 5f
                        centerRadius = 30f
                        start()
                    }
                )
                .into(binding.ivItemPhoto)

            // Name
            binding.tvItemName.text = story.name

            // Date
            binding.tvItemDate.text = story.createdAt.formatToRelativeTime()

            // Description
            binding.tvItemDescription.text = story.description

            // Click listener
            itemView.setOnClickListener {
                onItemClickListener?.invoke(story)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {

            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}