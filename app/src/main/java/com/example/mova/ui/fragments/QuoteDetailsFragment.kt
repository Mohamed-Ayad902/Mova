package com.example.mova.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mova.databinding.FragmentQuoteDetailsBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Resource

private const val TAG = "QuoteDetailsFrt mohamed"

class QuoteDetailsFragment : Fragment() {

    private val args: QuoteDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: MoviesViewModel

    private lateinit var binding: FragmentQuoteDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuoteDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel
        val slug = args.slug

        if (slug != null) {
            viewModel.getQuote(slug)
            binding.btnNext.visibility = View.INVISIBLE
        } else
            viewModel.getQuote()

        viewModel.quote.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "quote observe loading: ")
                    loadingQuote(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "quote observe success: ")
                    loadingQuote(false)
                    response.data?.let {
                        binding.apply {
                            tvQuote.text = it.quote
                            tvRole.text = it.role
                            tvShow.text = it.show
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "quote observe error: ${response.message} ")
                    loadingQuote(false)
                    binding.apply {
                        tvQuote.text = "Error while getting quote\n${response.message}"
                        tvRole.text = ""
                        tvShow.text = ""
                    }
                }
            }

        }


    }

    private fun loadingQuote(loading: Boolean) {
        if (loading) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                btnNext.isEnabled = false
            }
        } else {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                btnNext.isEnabled = true
            }
        }
    }


}