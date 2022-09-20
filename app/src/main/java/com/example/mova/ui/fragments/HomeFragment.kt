package com.example.mova.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.mova.R
import com.example.mova.adapters.*
import com.example.mova.adapters.callbacks.OnMovieClickListener
import com.example.mova.adapters.callbacks.OnPopularPeopleClickListener
import com.example.mova.adapters.callbacks.OnTvShowClickListener
import com.example.mova.data.movie.Movie
import com.example.mova.data.people.Result
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.FragmentHomeBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Constants
import com.example.mova.utils.Resource
import com.facebook.shimmer.ShimmerFrameLayout

private const val TAG = "HomeFragment mohamed"

class HomeFragment : Fragment() {
    private lateinit var viewModel: MoviesViewModel

    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var topMoviesAdapter: TopMoviesSlideAdapter
    private lateinit var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter
    private lateinit var upComingMoviesAdapter: UpComingMoviesAdapter
    private lateinit var topTvShowsAdapter: TopTvShowsSlideAdapter
    private lateinit var popularTvShowsAdapter: PopularTvShowsAdapter
    private lateinit var airingTodayTvShowsAdapter: AiringTodayTvShowsAdapter
    private lateinit var onAirTvShowsAdapter: OnAirTvShowsAdapter
    private lateinit var popularPeopleAdapter: PopularPeopleAdapter

    private lateinit var topMoviesHandler: Handler
    private lateinit var topTvShowsHandler: Handler
    private lateinit var binding: FragmentHomeBinding

    private val topMoviesRunnable = Runnable {
        binding.topRatedMoviesViewPager.currentItem =
            binding.topRatedMoviesViewPager.currentItem + 1
    }
    private val topTvShowsRunnable = Runnable {
        binding.topRatedTvShowViewPager.currentItem =
            binding.topRatedTvShowViewPager.currentItem + 1
    }


    override fun onPause() {
        super.onPause()
        topMoviesHandler.removeCallbacks(topMoviesRunnable)
        topTvShowsHandler.removeCallbacks(topTvShowsRunnable)
    }

    override fun onResume() {
        super.onResume()
        topMoviesHandler.postDelayed(topMoviesRunnable, 2000)
        topTvShowsHandler.postDelayed(topTvShowsRunnable, 2000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.apply {
            seeAllPopularCast.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_seeAllPeopleFragment)
            }
            seeAllTopMovies.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_TOP_MOVIES,
                        true
                    )
                )
            }
            seeAllPopularMovies.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_POPULAR_MOVIES,
                        true
                    )
                )
            }
            seeAllNowPlayingMovies.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_NOW_PLAYING_MOVIES,
                        true
                    )
                )
            }
            seeAllUpcomingMovies.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_UP_COMING_MOVIES,
                        true
                    )
                )
            }
            seeAllTopTvShows.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_TOP_TV,
                        false
                    )
                )
            }
            seeAllPopularTvShows.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_POPULAR_TV,
                        false
                    )
                )
            }
            seeAllAiringTodayTvShows.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_AIRING_TODAY_TV,
                        false
                    )
                )
            }
            seeAllOnAirTvShows.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSeeAllMoviesFragment(
                        Constants.DIR_ON_AIR_TV,
                        false
                    )
                )
            }
        }

        setupTopMoviesViewPager()
        setupPopularMovies()
        setupNowPlayingMovies()
        setupUpComingMovies()
        setupUpTopTvShows()
        setupPopularTvShows()
        setupAiringTodayTvShows()
        setupOnAirTvShows()
        setupPopularPeople()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        viewModel.topRatedMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "top movie observe: loading")
                    topShimmerEffect(
                        binding.topRatedMoviesViewPager,
                        binding.shimmerTopMovies,
                        true
                    )
                }
                is Resource.Success -> {
                    response.data?.let {
                        Log.d(TAG, "top movie observe: success")
                        topShimmerEffect(
                            binding.topRatedMoviesViewPager,
                            binding.shimmerTopMovies,
                            false
                        )
                        topMoviesAdapter.differ.submitList(it.results.subList(0, 6))
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "top movie observe: ${response.message}")
                    topShimmerEffect(
                        binding.topRatedMoviesViewPager,
                        binding.shimmerTopMovies,
                        false
                    )
                }
            }
        }

        viewModel.popularMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "popular movie observe: loading")
                    shimmerEffect(
                        binding.popularMoviesRecyclerView,
                        binding.shimmerPopularMovies,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "popular movie observe : success")
                    shimmerEffect(
                        binding.popularMoviesRecyclerView,
                        binding.shimmerPopularMovies,
                        false
                    )
                    response.data?.let {
                        it.results.shuffle()
                        popularMoviesAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "popular movie observe: ${response.message}")
                    shimmerEffect(
                        binding.popularMoviesRecyclerView,
                        binding.shimmerPopularMovies,
                        false
                    )
                }
            }
        }

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "now playing movie observe: loading")
                    shimmerEffect(
                        binding.nowPlayingMoviesRecyclerView,
                        binding.shimmerNowPlayingMovies,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "now playing movie observe : success")
                    shimmerEffect(
                        binding.nowPlayingMoviesRecyclerView,
                        binding.shimmerNowPlayingMovies,
                        false
                    )
                    response.data?.let {
                        it.results.shuffle()
                        nowPlayingMoviesAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "now playing movie observe: ${response.message}")
                    shimmerEffect(
                        binding.nowPlayingMoviesRecyclerView,
                        binding.shimmerNowPlayingMovies,
                        false
                    )
                }
            }
        }

        viewModel.upComingMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "up coming movie observe: loading")
                    shimmerEffect(
                        binding.upComingMoviesRecyclerView,
                        binding.shimmerUpComingMovies,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "up coming movie observe : success")
                    shimmerEffect(
                        binding.upComingMoviesRecyclerView,
                        binding.shimmerUpComingMovies,
                        false
                    )
                    response.data?.let {
                        upComingMoviesAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "up coming movie observe: ${response.message}")
                    shimmerEffect(
                        binding.upComingMoviesRecyclerView,
                        binding.shimmerUpComingMovies,
                        false
                    )
                }
            }
        }

        viewModel.topRatedTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "top TvShow observe: loading")
                    topShimmerEffect(
                        binding.topRatedTvShowViewPager,
                        binding.shimmerTopTvShows,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "top TvShow observe : success")
                    topShimmerEffect(
                        binding.topRatedTvShowViewPager,
                        binding.shimmerTopTvShows,
                        false
                    )
                    response.data?.let {
                        topTvShowsAdapter.differ.submitList(it.results.subList(0, 6))
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "top TvShow observe: ${response.message}")
                    topShimmerEffect(
                        binding.topRatedTvShowViewPager,
                        binding.shimmerTopTvShows,
                        false
                    )
                }
            }
        }

        viewModel.popularTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "popular TvShow observe: loading")
                    shimmerEffect(
                        binding.popularTvShowRecyclerView,
                        binding.shimmerPopularTvShow,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "popular TvShow observe : success")
                    shimmerEffect(
                        binding.popularTvShowRecyclerView,
                        binding.shimmerPopularTvShow,
                        false
                    )
                    response.data?.let {
                        popularTvShowsAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "popular TvShow observe: ${response.message}")
                    shimmerEffect(
                        binding.popularTvShowRecyclerView,
                        binding.shimmerPopularTvShow,
                        false
                    )
                }
            }
        }

        viewModel.airingTodayTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "airing today TvShow movie observe: loading")
                    shimmerEffect(
                        binding.airingTodayTvShowRecyclerView,
                        binding.shimmerAiringToday,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "airing today TvShow movie observe : success")
                    shimmerEffect(
                        binding.airingTodayTvShowRecyclerView,
                        binding.shimmerAiringToday,
                        false
                    )
                    response.data?.let {
                        it.results.shuffle()
                        airingTodayTvShowsAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "airing today TvShow movie observe: ${response.message}")
                    shimmerEffect(
                        binding.airingTodayTvShowRecyclerView,
                        binding.shimmerAiringToday,
                        false
                    )
                }
            }
        }

        viewModel.onAirTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "on air today TvShow observe: loading")
                    shimmerEffect(
                        binding.onAirTvShowRecyclerView,
                        binding.shimmerOnAirTvShow,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "on air TvShow observe : success")
                    shimmerEffect(
                        binding.onAirTvShowRecyclerView,
                        binding.shimmerOnAirTvShow,
                        false
                    )
                    response.data?.let {
                        it.results.shuffle()
                        onAirTvShowsAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "on air today TvShow observe: ${response.message}")
                    shimmerEffect(
                        binding.onAirTvShowRecyclerView,
                        binding.shimmerOnAirTvShow,
                        false
                    )
                }
            }
        }

        viewModel.popularPeople.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "popular people observe: loading")
                    shimmerEffect(
                        binding.popularPeopleRecyclerView,
                        binding.shimmerPopularCast,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "popular people observe : success")
                    shimmerEffect(
                        binding.popularPeopleRecyclerView,
                        binding.shimmerPopularCast,
                        false
                    )
                    response.data?.let {
                        popularPeopleAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "popular people observe: ${response.message}")
                    shimmerEffect(
                        binding.popularPeopleRecyclerView,
                        binding.shimmerPopularCast,
                        false
                    )
                }
            }
        }

    }

    private fun topShimmerEffect(
        viewPager: ViewPager2,
        shimmer: ShimmerFrameLayout,
        loading: Boolean
    ) {
        if (loading) {
            viewPager.visibility = View.INVISIBLE
            shimmer.visibility = View.VISIBLE
        } else {
            shimmer.visibility = View.INVISIBLE
            viewPager.visibility = View.VISIBLE
        }
    }

    private fun shimmerEffect(
        recyclerView: RecyclerView,
        shimmerLayout: ShimmerFrameLayout,
        loading: Boolean
    ) {
        if (loading) {
            recyclerView.visibility = View.INVISIBLE
            shimmerLayout.visibility = View.VISIBLE
        } else {
            shimmerLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupTopMoviesViewPager() {
        topMoviesHandler = Handler(Looper.myLooper()!!)
        topMoviesAdapter =
            TopMoviesSlideAdapter(object : OnMovieClickListener {
                override fun onClick(movie: Movie) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(
                            movie.id,
                            null
                        )
                    )
                }
            }, requireContext(), binding.topRatedMoviesViewPager)
        binding.topRatedMoviesViewPager.adapter = topMoviesAdapter
        binding.topRatedMoviesViewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        // transform effect
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        binding.topRatedMoviesViewPager.setPageTransformer(transformer)

        // looping effect
        binding.topRatedMoviesViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                topMoviesHandler.removeCallbacks(topMoviesRunnable)
                topMoviesHandler.postDelayed(topMoviesRunnable, 3000)
            }
        })
    }

    private fun setupPopularMovies() {
        popularMoviesAdapter =
            PopularMoviesAdapter(object : OnMovieClickListener {
                override fun onClick(movie: Movie) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(
                            movie.id,
                            null
                        )
                    )
                }
            }, requireContext())
        binding.popularMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }
    }

    private fun setupNowPlayingMovies() {
        nowPlayingMoviesAdapter =
            NowPlayingMoviesAdapter(object : OnMovieClickListener {
                override fun onClick(movie: Movie) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(
                            movie.id,
                            null
                        )
                    )
                }
            }, requireContext())
        binding.nowPlayingMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = nowPlayingMoviesAdapter
        }
    }

    private fun setupUpComingMovies() {
        upComingMoviesAdapter =
            UpComingMoviesAdapter(object : OnMovieClickListener {
                override fun onClick(movie: Movie) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(
                            movie.id,
                            null
                        )
                    )
                }
            }, requireContext())
        binding.upComingMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = upComingMoviesAdapter
        }
    }

    private fun setupUpTopTvShows() {
        topTvShowsHandler = Handler(Looper.myLooper()!!)
        topTvShowsAdapter =
            TopTvShowsSlideAdapter(object : OnTvShowClickListener {
                override fun onClick(tv: Tv) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToTvShowDetailsFragment(
                            tv.id,
                            null
                        )
                    )
                }
            }, requireContext(), binding.topRatedTvShowViewPager)
        binding.topRatedTvShowViewPager.adapter = topTvShowsAdapter
        binding.topRatedTvShowViewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        // transform effect
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        binding.topRatedTvShowViewPager.setPageTransformer(transformer)

        // looping effect
        binding.topRatedTvShowViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                topTvShowsHandler.removeCallbacks(topTvShowsRunnable)
                topTvShowsHandler.postDelayed(topTvShowsRunnable, 3000)
            }
        })
    }

    private fun setupPopularTvShows() {
        popularTvShowsAdapter =
            PopularTvShowsAdapter(object : OnTvShowClickListener {
                override fun onClick(tv: Tv) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToTvShowDetailsFragment(
                            tv.id, null
                        )
                    )
                }
            }, requireContext())
        binding.popularTvShowRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = popularTvShowsAdapter
        }
    }

    private fun setupAiringTodayTvShows() {
        airingTodayTvShowsAdapter =
            AiringTodayTvShowsAdapter(object : OnTvShowClickListener {
                override fun onClick(tv: Tv) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToTvShowDetailsFragment(
                            tv.id,
                            null
                        )
                    )
                }
            }, requireContext())
        binding.airingTodayTvShowRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = airingTodayTvShowsAdapter
        }
    }

    private fun setupOnAirTvShows() {
        onAirTvShowsAdapter =
            OnAirTvShowsAdapter(object : OnTvShowClickListener {
                override fun onClick(tv: Tv) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToTvShowDetailsFragment(
                            tv.id,
                            null
                        )
                    )
                }
            }, requireContext())
        binding.onAirTvShowRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = onAirTvShowsAdapter
        }
    }

    private fun setupPopularPeople() {
        popularPeopleAdapter =
            PopularPeopleAdapter(object : OnPopularPeopleClickListener {
                override fun onClick(result: Result) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToPeopleFragment(
                            result.id
                        )
                    )
                }
            }, requireContext())
        binding.popularPeopleRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = popularPeopleAdapter
        }
    }

}