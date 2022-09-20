package com.example.mova.ui.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.mova.R
import com.example.mova.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.apply {
            title.startAnimation(fadeIn)
            description.startAnimation(fadeIn)
            imageView.startAnimation(zoomIn)
        }
    }

    override fun onPause() {
        super.onPause()
        val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        binding.apply {
            title.startAnimation(fadeOut)
            description.startAnimation(fadeOut)
            imageView.startAnimation(zoomOut)
        }
    }

}