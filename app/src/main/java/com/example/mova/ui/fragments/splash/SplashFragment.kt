package com.example.mova.ui.fragments.splash

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mova.R
import com.example.mova.databinding.FragmentSplashBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.utils.Constants.Companion.ON_BOARDING_FINISHED
import com.example.mova.utils.Constants.Companion.ON_BOARDING_SHARED_PREF

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)


        val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)
        binding.animationView.apply {
            startAnimation(bounce)
            playAnimation()
            repeatCount = 1
            speed = 1.5f
        }

        val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
        Handler().postDelayed({
            binding.textView.startAnimation(zoomIn)
        }, 450)

        val explode = AnimationUtils.loadAnimation(context, R.anim.explode)
        Handler().postDelayed({
            binding.view.startAnimation(explode)
        }, 2500)


        Handler().postDelayed({
            if (onBoardingFinished()) {
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
                )
                requireActivity().finish()
            } else
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingHolderFragment)
        }, 2750)

        return binding.root
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref =
            requireActivity().getSharedPreferences(ON_BOARDING_SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(ON_BOARDING_FINISHED, false)
    }

}