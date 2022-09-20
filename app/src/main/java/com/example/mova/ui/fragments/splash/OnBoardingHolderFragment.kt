package com.example.mova.ui.fragments.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mova.R
import com.example.mova.adapters.ViewPagerAdapter
import com.example.mova.databinding.FragmentOnBoardingHolderBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.utils.Constants.Companion.ON_BOARDING_FINISHED
import com.example.mova.utils.Constants.Companion.ON_BOARDING_SHARED_PREF

class OnBoardingHolderFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingHolderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingHolderBinding.inflate(layoutInflater, container, false)

        val fragments = listOf(FirstFragment(), SecondFragment(), ThirdFragment())
        val mAdapter =
            ViewPagerAdapter(
                fragments,
                fm = requireActivity().supportFragmentManager,
                lifecycle =  lifecycle
            )

        binding.viewPager2.adapter = mAdapter
        setupIndicators()
        setCurrentIndicator(0)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.previous.visibility = View.INVISIBLE
                    2 -> binding.next.text = "Get started"
                    else -> {
                        binding.next.text = "Next"
                        binding.previous.visibility = View.VISIBLE
                    }
                }
                setCurrentIndicator(position)
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })

        binding.next.setOnClickListener {
            if (getItem() > binding.viewPager2.childCount) {
                finishOnBoarding()
                startActivity(Intent(context, HomeActivity::class.java))
                requireActivity().finish()
            } else {
                binding.viewPager2.setCurrentItem(getItem() + 1, true)
            }
        }

        binding.previous.setOnClickListener {
            if (getItem() == 0) {
                requireActivity().finish()
            } else {
                binding.viewPager2.setCurrentItem(getItem() - 1, true)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (getItem() == 0) {
                        requireActivity().finish()
                    } else {
                        binding.viewPager2.setCurrentItem(getItem() - 1, true)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun getItem(): Int {
        return binding.viewPager2.currentItem
    }

    private fun finishOnBoarding() {
        val sharedPref =
            requireActivity().getSharedPreferences(ON_BOARDING_SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(ON_BOARDING_FINISHED, true)
        editor.apply()
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(3)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

}