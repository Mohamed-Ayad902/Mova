package com.example.mova.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.*
import com.example.mova.adapters.callbacks.OnCastClickListener
import com.example.mova.adapters.callbacks.OnCrewClickListener
import com.example.mova.adapters.callbacks.OnMovieClickListener
import com.example.mova.data.movie.Cast
import com.example.mova.data.movie.Crew
import com.example.mova.data.movie.Movie
import com.example.mova.databinding.FragmentMovieDetailsBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.ui.TrailerActivity
import com.example.mova.utils.Constants
import com.example.mova.utils.Constants.Companion.IMAGE_URL
import com.example.mova.utils.Resource
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MovieDetailsF mohamed"

class MovieDetailsFragment : Fragment() {

    private lateinit var castAdapter: CastAdapter
    private lateinit var crewAdapter: CrewAdapter
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var similarMoviesAdapter: SimilarMoviesAdapter
    private lateinit var recommendedMoviesAdapter: RecommendedMoviesAdapter
    private lateinit var viewModel: MoviesViewModel
    private val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentMovieDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)
        binding.back.setOnClickListener { requireActivity().onBackPressed() }

        setupCast()
        setupCrew()
        setupSimilarMovies()
        setupGenreAdapter()
        setupRecommendedMovies()
        return binding.root
    }

    private fun setupGenreAdapter() {
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            genreAdapter = GenreAdapter()
            adapter = genreAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel
        val id = args.movieId
        viewModel.getMovie(id)
        viewModel.getRecommendedMovies(id)
        viewModel.getSimilarMovies(id)
        viewModel.getMovieCredits(id)
        viewModel.getMovieTrailer(id)


        viewModel.movieTrailer.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.btnTrailer.isClickable = false
                    Log.d(TAG, "trailer observe loading:")
                }
                is Resource.Success -> {
                    binding.btnTrailer.isClickable = true
                    response.data?.let { trailerResponse ->
                        Log.d(
                            TAG,
                            "trailer observe success: ${trailerResponse.results.size} ${trailerResponse.results}"
                        )
                        if (trailerResponse.results.isNotEmpty()) {
                            val s = trailerResponse.results.filter {
                                it.type == "Trailer" && it.official
                            }
                            Log.d(TAG, "trailer filters :${s.size} $s ")

                            binding.btnTrailer.setOnClickListener {
                                val extra: String = if (s.isNotEmpty()) {
                                    s[0].key
                                } else
                                    trailerResponse.results[0].key

                                startActivity(
                                    Intent(
                                        requireContext(),
                                        TrailerActivity::class.java
                                    ).putExtra(
                                        Constants.EXTRA_TRAILER,
                                        extra
                                    )
                                )
                            }
                        } else {
                            binding.btnTrailer.setOnClickListener {
                                Toast.makeText(requireContext(), "Un Available", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    binding.btnTrailer.isClickable = false
                    Log.e(TAG, "trailer observe error: ${response.message} ")
                }
            }
        }


        var savedMovie: Movie? = null
        viewModel.getSavedMovie(id)
            .observe(viewLifecycleOwner) { movie ->      // this to check if we already saved this movie before
                if (movie == null) {
                    binding.save.apply {
                        setImageResource(R.drawable.ic_save)
                        setOnClickListener {
                            viewModel.insertMovie(savedMovie!!)
                            Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    binding.save.apply {
                        setImageResource(R.drawable.ic_saved)
                        setOnClickListener {
                            viewModel.deleteMovie(movie)
                            Snackbar.make(requireView(), "UnSaved", Snackbar.LENGTH_LONG)
                                .setAction("Undo") {
                                    viewModel.insertMovie(movie)
                                }.show()
                        }
                    }
                }
            }

        viewModel.movie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "movie details observe: loading")
                    loadingMovie(true)
                }
                is Resource.Success -> {
                    loadingMovie(false)
                    response.data?.let { movie ->
                        savedMovie = movie
                        genreAdapter.differ.submitList(movie.genres)
                        binding.tvOverView.text = movie.overview
                        binding.tvName.text = movie.original_title
                        binding.tvName.isSelected = true
                        binding.tvTime.text = movie.runtime.toString()
                        binding.tvRating.text = movie.vote_average.toString()
                        binding.tvRating.text = String.format("%.01f", movie.vote_average)
                        if (movie.budget == 0)
                            binding.tvBudget.text = "Unknown"
                        else
                            binding.tvBudget.text = movie.budget.toString()
                        movie.poster_path?.let {
                            Glide.with(requireContext()).load(IMAGE_URL + it)
                                .into(binding.imageView)
                            Glide.with(requireContext()).load(IMAGE_URL + it)
                                .into(binding.imageViewTrailer)
                        } ?: run {
                            Glide.with(requireContext()).load(R.drawable.series)
                                .into(binding.imageView)
                            Glide.with(requireContext()).load(R.drawable.series)
                                .into(binding.imageViewTrailer)
                        }
                        if (movie.adult)
                            binding.tvAdult.text = "18+"
                        else
                            binding.tvAdult.text = "18-"

                        binding.save.setOnClickListener {
                            viewModel.insertMovie(movie)
                            binding.save.setImageResource(R.drawable.ic_saved)
                        }
                    }

                }
                is Resource.Error -> {
                    Log.e(TAG, "movie details observe : ${response.message}")
                    loadingMovie(false)
                }
            }
        }

        viewModel.similarMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "similar movie observe: loading")
                    loadingSimilar(binding.similarRecyclerView, binding.shimmerSimilarMovies, true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "similar movies observe :success")
                    loadingSimilar(binding.similarRecyclerView, binding.shimmerSimilarMovies, false)
                    response.data?.let { list ->
                        similarMoviesAdapter.differ.submitList(list.results)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "similar movies observe : ${response.message}")
                    loadingSimilar(binding.similarRecyclerView, binding.shimmerSimilarMovies, false)
                }
            }
        }

        viewModel.recommendedMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "recommended movie observe: loading")
                    loadingRecommended(
                        binding.recommendedRecyclerView,
                        binding.shimmerRecommendedMovies,
                        true
                    )
                }
                is Resource.Success -> {
                    Log.d(TAG, "recommended movies observe :success")
                    loadingRecommended(
                        binding.recommendedRecyclerView,
                        binding.shimmerRecommendedMovies,
                        false
                    )
                    response.data?.let { list ->
                        recommendedMoviesAdapter.differ.submitList(list.results)
                    }

                }
                is Resource.Error -> {
                    Log.e(TAG, "recommended movies observe : ${response.message}")
                    loadingRecommended(
                        binding.recommendedRecyclerView,
                        binding.shimmerRecommendedMovies,
                        false
                    )
                }
            }

        }

        viewModel.movieCredits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "movie credits observe: loading")
                    loadingCredits(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "movie credits observe :success")
                    loadingCredits(false)
                    response.data?.let { list ->
                        castAdapter.differ.submitList(list.cast)
                        crewAdapter.differ.submitList(list.crew)
                    }

                }
                is Resource.Error -> {
                    Log.e(TAG, "movie credits observe : ${response.message}")
                    loadingCredits(false)
                }
            }
        }


    }

    private fun loadingCredits(loading: Boolean) {
        if (loading) {
            binding.crewRecyclerView.visibility = View.INVISIBLE
            binding.castRecyclerView.visibility = View.INVISIBLE
            binding.shimmerCrew.visibility = View.VISIBLE
            binding.shimmerCast.visibility = View.VISIBLE
        } else {
            binding.crewRecyclerView.visibility = View.VISIBLE
            binding.castRecyclerView.visibility = View.VISIBLE
            binding.shimmerCrew.visibility = View.INVISIBLE
            binding.shimmerCast.visibility = View.INVISIBLE
        }
    }

    private fun loadingRecommended(
        recyclerView: RecyclerView,
        shimmerRecommendedMovies: ShimmerFrameLayout,
        loading: Boolean
    ) {
        if (loading) {
            recyclerView.visibility = View.INVISIBLE
            shimmerRecommendedMovies.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            shimmerRecommendedMovies.visibility = View.INVISIBLE
        }
    }

    private fun loadingSimilar(
        recyclerView: RecyclerView,
        shimmerSimilarMovies: ShimmerFrameLayout,
        loading: Boolean
    ) {
        if (loading) {
            recyclerView.visibility = View.INVISIBLE
            shimmerSimilarMovies.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            shimmerSimilarMovies.visibility = View.INVISIBLE
        }
    }

    private fun loadingMovie(loading: Boolean) {
        if (loading) {
            binding.imageView.visibility = View.INVISIBLE
            binding.shimmerImageView.visibility = View.VISIBLE
            binding.tvOverView.visibility = View.INVISIBLE
            binding.shimmerOverView.visibility = View.VISIBLE
        } else {
            binding.imageView.visibility = View.VISIBLE
            binding.shimmerImageView.visibility = View.INVISIBLE
            binding.tvOverView.visibility = View.VISIBLE
            binding.shimmerOverView.visibility = View.INVISIBLE
        }
    }

    private fun setupCast() {
        castAdapter = CastAdapter(object : OnCastClickListener {
            override fun onClick(cast: Cast) {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPeopleFragment(
                        cast.id
                    )
                )
            }
        }, requireContext())
        binding.castRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = castAdapter
        }
    }

    private fun setupCrew() {
        crewAdapter = CrewAdapter(object : OnCrewClickListener {
            override fun onClick(crew: Crew) {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPeopleFragment(
                        crew.id
                    )
                )
            }
        }, requireContext())
        binding.crewRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = crewAdapter
        }
    }

    private fun setupRecommendedMovies() {
        recommendedMoviesAdapter = RecommendedMoviesAdapter(object : OnMovieClickListener {
            override fun onClick(movie: Movie) {
                Log.d(TAG, "recommended onClick: $movie")
                loadMovie(movie.id)
            }
        }, requireContext())
        binding.recommendedRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = recommendedMoviesAdapter
        }
    }

    private fun setupSimilarMovies() {
        similarMoviesAdapter = SimilarMoviesAdapter(object : OnMovieClickListener {
            override fun onClick(movie: Movie) {
                Log.d(TAG, "similar onClick: $movie")
                loadMovie(movie.id)
            }
        }, requireContext())
        binding.similarRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = similarMoviesAdapter
        }
    }

    private fun loadMovie(id: Int) {
        viewModel.getMovie(id)
        viewModel.getMovieCredits(id)
        viewModel.getSimilarMovies(id)
        viewModel.getRecommendedMovies(id)
        viewModel.getMovieTrailer(id)

    }

}