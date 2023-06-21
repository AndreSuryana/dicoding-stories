package com.andresuryana.dicodingstories.ui.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.data.source.prefs.SessionHelper
import com.andresuryana.dicodingstories.databinding.FragmentSplashBinding
import com.andresuryana.dicodingstories.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var session: SessionHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check user session, after 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserSession()
        }, 1500L)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clear layout binding
        _binding = null
    }

    private fun checkUserSession() {
        if (session.isLoggedIn()) {
            getNavController().navigate(R.id.action_splashFragment_to_listStoryFragment)
        } else {
            getNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}