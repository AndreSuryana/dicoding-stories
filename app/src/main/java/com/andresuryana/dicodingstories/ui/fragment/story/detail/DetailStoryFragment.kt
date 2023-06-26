package com.andresuryana.dicodingstories.ui.fragment.story.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.databinding.FragmentDetailStoryBinding
import com.andresuryana.dicodingstories.ui.base.BaseFragment
import com.andresuryana.dicodingstories.util.Ext.formatDate
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryFragment : BaseFragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailStoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get story from arguments
        val story: Story? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(getString(R.string.key_story), Story::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(getString(R.string.key_story))
        }

        // Set story data
        if (story != null) {
            setStory(story)
        }

        // Setup button
        binding.buttonBack.setOnClickListener { getNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clear layout binding
        _binding = null
    }

    private fun setStory(story: Story) {
        // Load image
        Glide.with(requireContext())
            .load(story.photoUrl)
            .placeholder(
                CircularProgressDrawable(requireContext()).apply {
                    strokeWidth = 5f
                    centerRadius = 30f
                    start()
                }
            )
            .centerCrop()
            .into(binding.ivDetailPhoto)

        // Name
        binding.tvDetailName.text = story.name

        // Date
        binding.tvDetailDate.text = story.createdAt.formatDate("dd/mm/yyyy")

        // Description
        binding.tvDetailDescription.text = story.description
    }
}