package com.example.mova.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mova.adapters.AllMoviesAdapter
import com.example.mova.adapters.AllTvShowAdapter
import com.example.mova.adapters.callbacks.OnMovieClickListener
import com.example.mova.adapters.callbacks.OnTvShowClickListener
import com.example.mova.data.movie.Movie
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.FragmentSeeAllMoviesBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Constants
import com.example.mova.utils.Resource

private const val TAG = "SeeMoviesFrt mohamed"

class SeeAllMoviesFragment : Fragment() {

    private val args: SeeAllMoviesFragmentArgs by navArgs()
    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesAdapter: AllMoviesAdapter
    private lateinit var tvAdapter: AllTvShowAdapter

    private lateinit var binding: FragmentSeeAllMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeeAllMoviesBinding.inflate(layoutInflater, container, false)
        if (args.isMovie)
            setupMoviesRecyclerView()
        else
            setupTvRecyclerView()

        return binding.root
    }

    private fun setupMoviesRecyclerView() {
        moviesAdapter = AllMoviesAdapter(object : OnMovieClickListener {
            override fun onClick(movie: Movie) {
                findNavController().navigate(
                    SeeAllMoviesFragmentDirections.actionSeeAllMoviesFragmentToMovieDetailsFragment(
                        movie.id
                        ,null
                    )
                )
            }
        }, requireContext())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
            addOnScrollListener(this@SeeAllMoviesFragment.moviesScrollListener)
        }
    }

    private fun setupTvRecyclerView() {
        tvAdapter = AllTvShowAdapter(object : OnTvShowClickListener {
            override fun onClick(tv: Tv) {
                findNavController().navigate(
                    SeeAllMoviesFragmentDirections.actionSeeAllMoviesFragmentToTvShowDetailsFragment(
                        tv.id
                        ,null
                    )
                )
            }
        }, requireContext())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tvAdapter
            addOnScrollListener(this@SeeAllMoviesFragment.tvScrollListener)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel

        if (args.isMovie)
            when (args.diriction) {
                Constants.DIR_TOP_MOVIES -> topRatedMovies()
                Constants.DIR_POPULAR_MOVIES -> popularMovies()
                Constants.DIR_UP_COMING_MOVIES -> upComingMovies()
                Constants.DIR_NOW_PLAYING_MOVIES -> nowPlayingMovies()
            }
        else
            when (args.diriction) {
                Constants.DIR_TOP_TV -> topRatedTv()
                Constants.DIR_POPULAR_TV -> popularTv()
                Constants.DIR_AIRING_TODAY_TV -> airingToday()
                Constants.DIR_ON_AIR_TV -> onAir()
            }

    }

    private fun onAir() {
        viewModel.getOnAirTvShow()

        viewModel.onAirTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "on air today TvShow observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "on air TvShow observe : success")
                    loading(false)
                    response.data?.let {
                        tvAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "on air today TvShow observe: ${response.message}")
                    loading(false)
                }
            }
        }

    }

    private fun airingToday() {
        viewModel.getAiringTodayTvShow()

        viewModel.airingTodayTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "airing today TvShow movie observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "airing today TvShow movie observe : success")
                    loading(false)
                    response.data?.let {
                        tvAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "airing today TvShow movie observe: ${response.message}")
                    loading(false)
                }
            }
        }

    }

    private fun popularTv() {
        viewModel.getPopularTvShow()

        viewModel.popularTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "popular TvShow observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "popular TvShow observe : success")
                    loading(false)
                    response.data?.let {
                        tvAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "popular TvShow observe: ${response.message}")
                    loading(false)
                }
            }
        }

    }

    private fun topRatedTv() {
        viewModel.getTopRatedTvShow()

        viewModel.topRatedTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "top TvShow observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "top TvShow observe : success")
                    loading(false)
                    response.data?.let {
                        tvAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "top TvShow observe: ${response.message}")
                    loading(false)
                }
            }
        }

    }

    var isMoviesLoading = false
    var isMoviesLastPage = false
    var isMoviesScrolling = false

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
                when (args.diriction) {
                    Constants.DIR_TOP_MOVIES -> {
                        viewModel.getTopRatedMovies()
                    }
                    Constants.DIR_POPULAR_MOVIES -> {
                        viewModel.getPopularMovies()
                    }
                    Constants.DIR_UP_COMING_MOVIES -> {
                        viewModel.getUpComingMovies()
                    }
                    Constants.DIR_NOW_PLAYING_MOVIES -> {
                        viewModel.getNowPlayingMovies()
                    }
                }
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

    var isTvLoading = false
    var isTvLastPage = false
    var isTvScrolling = false

    private val tvScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLadingAndNotLastPage = !isTvLoading && !isTvLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreTanVisible = totalItemCount >= Constants.PAGE_SIZE
            val shouldPaginate =
                isNotLadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreTanVisible && isTvScrolling

            if (shouldPaginate) {
                when (args.diriction) {
                    Constants.DIR_POPULAR_TV -> {
                        viewModel.getPopularTvShow()
                    }
                    Constants.DIR_AIRING_TODAY_TV -> {
                        viewModel.getAiringTodayTvShow()
                    }
                    Constants.DIR_ON_AIR_TV -> {
                        viewModel.getOnAirTvShow()
                    }
                    Constants.DIR_TOP_TV -> {
                        viewModel.getTopRatedTvShow()
                    }
                }
                isTvScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isTvScrolling = true
            }
        }
    }

    private fun nowPlayingMovies() {
        viewModel.getNowPlayingMovies()

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "now playing movie observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "now playing movie observe : success")
                    loading(false)
                    response.data?.let {
                        moviesAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "now playing movie observe: ${response.message}")
                    loading(false)
                }
            }
        }
    }

    private fun upComingMovies() {
        viewModel.getUpComingMovies()

        viewModel.upComingMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "up coming movie observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "up coming movie observe : success")
                    response.data?.let {
                        moviesAdapter.differ.submitList(it.results)
                        loading(false)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "up coming movie observe: ${response.message}")
                    loading(false)
                }
            }
        }
    }

    private fun popularMovies() {
        viewModel.getPopularMovies()

        viewModel.popularMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "popular movie observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "popular movie observe : success")
                    loading(false)
                    response.data?.let {
                        moviesAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "popular movie observe: ${response.message}")
                    loading(false)
                }
            }
        }
    }

    private fun topRatedMovies() {
        viewModel.getTopRatedMovies()

        viewModel.topRatedMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "top movie observe: loading")
                    loading(true)
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(TAG, "top movie observe: success")
                        moviesAdapter.differ.submitList(it.results)
                        loading(false)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "top movie observe: ${response.message}")
                    loading(false)
                }
            }

        }

    }

    private fun loading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else
            binding.progressBar.visibility = View.INVISIBLE
    }

}