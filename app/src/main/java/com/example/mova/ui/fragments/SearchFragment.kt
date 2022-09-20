package com.example.mova.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mova.adapters.SearchedMoviesAdapter
import com.example.mova.adapters.SearchedPeopleAdapter
import com.example.mova.adapters.SearchedTvShowAdapter
import com.example.mova.adapters.callbacks.OnMovieClickListener
import com.example.mova.adapters.callbacks.OnPopularPeopleClickListener
import com.example.mova.adapters.callbacks.OnTvShowClickListener
import com.example.mova.data.movie.Movie
import com.example.mova.data.people.Result
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.FragmentSearchBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Constants
import com.example.mova.utils.Constants.Companion.SEARCH_DELAY_TIME
import com.example.mova.utils.Resource
import kotlinx.coroutines.*

private const val TAG = "SearchFragment mohamed"

class SearchFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesAdapter: SearchedMoviesAdapter
    private lateinit var seriesAdapter: SearchedTvShowAdapter
    private lateinit var peopleAdapter: SearchedPeopleAdapter

    private lateinit var binding: FragmentSearchBinding

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        setupMoviesRecyclerView()
        setupSeriesRecyclerView()
        setupPeopleRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel



        binding.radioGroup.setOnCheckedChangeListener { _, p1 ->
            when (p1) {
                binding.btnMovies.id -> {
                    binding.moviesRecyclerView.visibility = View.VISIBLE
                    binding.seriesRecyclerView.visibility = View.INVISIBLE
                    binding.peopleRecyclerView.visibility = View.INVISIBLE
                }
                binding.btnSeries.id -> {
                    binding.moviesRecyclerView.visibility = View.INVISIBLE
                    binding.seriesRecyclerView.visibility = View.VISIBLE
                    binding.peopleRecyclerView.visibility = View.INVISIBLE
                }
                else -> {
                    binding.moviesRecyclerView.visibility = View.INVISIBLE
                    binding.seriesRecyclerView.visibility = View.INVISIBLE
                    binding.peopleRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch(Dispatchers.IO) {
                delay(SEARCH_DELAY_TIME)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchMovies(editable.toString())
                        viewModel.searchSeries(editable.toString())
                        viewModel.searchPeople(editable.toString())
                    }
                }
            }
        }

        viewModel.searchMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "searching movies loading: ")
                    binding.progressBar.visibility = View.VISIBLE
                    isMoviesLoading = true
                }
                is Resource.Success -> {
                    Log.d(TAG, "searching movies success: size ${response.data?.results?.size}")
                    response.data?.let {
                        moviesAdapter.differ.submitList(it.results.toList())    // added .toList()
                        val totalPages = it.total_results / Constants.PAGE_SIZE + 2
                        isMoviesLastPage = viewModel.searchMoviesPage == totalPages
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                    isMoviesLoading = false
                }
                is Resource.Error -> {
                    Log.e(TAG, "searching movies error: ${response.message}")
                    binding.progressBar.visibility = View.INVISIBLE
                    isMoviesLoading = false
                }
            }
        }

        viewModel.searchSeries.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "searching series loading: ")
                    binding.progressBar.visibility = View.VISIBLE
                    isSeriesLoading = true
                }
                is Resource.Success -> {
                    Log.d(TAG, "searching series success:")
                    response.data?.let {
                        seriesAdapter.differ.submitList(it.results)
                        val totalPages = it.total_results / Constants.PAGE_SIZE + 2
                        isSeriesLastPage = viewModel.searchSeriesPage == totalPages
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                    isSeriesLoading = false
                }
                is Resource.Error -> {
                    Log.e(TAG, "searching series error: ${response.message}")
                    binding.progressBar.visibility = View.INVISIBLE
                    isSeriesLoading = false
                }
            }
        }

        viewModel.searchPeople.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "searching people loading: ")
                    binding.progressBar.visibility = View.VISIBLE
                    isPeopleLoading = true
                }
                is Resource.Success -> {
                    Log.d(TAG, "searching people success:")
                    response.data?.let {
                        peopleAdapter.differ.submitList(it.results)
                        val totalPages = it.total_results / Constants.PAGE_SIZE + 2
                        isPeopleLastPage = viewModel.searchPeoplePage == totalPages
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                    isPeopleLoading = false
                }
                is Resource.Error -> {
                    Log.e(TAG, "searching people error: ${response.message}")
                    binding.progressBar.visibility = View.INVISIBLE
                    isPeopleLoading = false
                }
            }
        }


    }

    var isMoviesLoading = false
    var isMoviesLastPage = false
    var isMoviesScrolling = false

    var isSeriesLoading = false
    var isSeriesLastPage = false
    var isSeriesScrolling = false

    var isPeopleLoading = false
    var isPeopleLastPage = false
    var isPeopleScrolling = false

    private val moviesScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLadingAndNotLastPage = !isMoviesLoading && !isMoviesLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreTanVisible = totalItemCount >= Constants.PAGE_SIZE
            val shouldPaginate =
                isNotLadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreTanVisible && isMoviesScrolling

            if (shouldPaginate) {
                viewModel.searchMovies(binding.etSearch.text.toString())
                isMoviesScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isMoviesScrolling = true
            }
        }
    }

    private val seriesScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLadingAndNotLastPage = !isSeriesLoading && !isSeriesLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreTanVisible = totalItemCount >= Constants.PAGE_SIZE
            val shouldPaginate =
                isNotLadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreTanVisible && isMoviesScrolling

            if (shouldPaginate) {
                viewModel.searchSeries(binding.etSearch.text.toString())
                isSeriesScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isSeriesScrolling = true
            }
        }
    }

    private val peopleScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLadingAndNotLastPage = !isPeopleLoading && !isPeopleLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreTanVisible = totalItemCount >= Constants.PAGE_SIZE
            val shouldPaginate =
                isNotLadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreTanVisible && isMoviesScrolling

            if (shouldPaginate) {
                viewModel.searchPeople(binding.etSearch.text.toString())
                isPeopleScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isPeopleScrolling = true
            }
        }
    }

    private fun setupMoviesRecyclerView() {
        moviesAdapter = SearchedMoviesAdapter(object : OnMovieClickListener {
            override fun onClick(movie: Movie) {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(
                        movie.id, null
                    )
                )
            }
        }, requireContext())

        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = moviesAdapter
            addOnScrollListener(this@SearchFragment.moviesScrollListener)
        }
    }

    private fun setupSeriesRecyclerView() {
        seriesAdapter = SearchedTvShowAdapter(object : OnTvShowClickListener {
            override fun onClick(tv: Tv) {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToTvShowDetailsFragment(
                        tv.id, null
                    )
                )
            }
        }, requireContext())

        binding.seriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = seriesAdapter
            addOnScrollListener(this@SearchFragment.seriesScrollListener)
        }
    }

    private fun setupPeopleRecyclerView() {
        peopleAdapter = SearchedPeopleAdapter(object : OnPopularPeopleClickListener {
            override fun onClick(result: Result) {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToPeopleFragment(
                        result.id
                    )
                )
            }
        }, requireContext())

        binding.peopleRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = peopleAdapter
            addOnScrollListener(this@SearchFragment.peopleScrollListener)
        }
    }

}