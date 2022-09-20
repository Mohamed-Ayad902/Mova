package com.example.mova.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mova.adapters.SeeAllPeopleAdapter
import com.example.mova.adapters.callbacks.OnPopularPeopleClickListener
import com.example.mova.data.people.Result
import com.example.mova.databinding.FragmentSeeAllPeopleBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Constants
import com.example.mova.utils.Resource

private const val TAG = "SeeAllPeopleFrt mohamed"

class SeeAllPeopleFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var peopleAdapter: SeeAllPeopleAdapter

    private lateinit var binding: FragmentSeeAllPeopleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeeAllPeopleBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() =
        binding.recyclerView.apply {
            peopleAdapter =
                SeeAllPeopleAdapter(object : OnPopularPeopleClickListener {
                    override fun onClick(result: Result) {
                        findNavController().navigate(
                            SeeAllPeopleFragmentDirections.actionSeeAllPeopleFragmentToPeopleFragment(
                                result.id
                            )
                        )
                    }
                }, requireContext())
            adapter = peopleAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@SeeAllPeopleFragment.scrollListener)
        }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreTanVisible = totalItemCount >= Constants.PAGE_SIZE
            val shouldPaginate =
                isNotLadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreTanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getPopularPeople()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun loading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else
            binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel

        viewModel.getPopularPeople()

        viewModel.popularPeople.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "see all popular people observe loading: ")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "see all popular people observe success: ")
                    response.data?.let {
                        Log.d(TAG, "top movie observe: success")
                        peopleAdapter.differ.submitList(it.results)
                        loading(false)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "see all popular people observe error :${response.message} ")
                    loading(false)
                }
            }
        }

    }

}