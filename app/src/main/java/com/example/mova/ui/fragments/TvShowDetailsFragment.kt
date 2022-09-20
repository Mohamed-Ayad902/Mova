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
import com.example.mova.adapters.callbacks.OnSeasonClickListener
import com.example.mova.adapters.callbacks.OnTvShowClickListener
import com.example.mova.data.movie.Cast
import com.example.mova.data.movie.Crew
import com.example.mova.data.tv.Season
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.FragmentTvShowDetailsBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.ui.TrailerActivity
import com.example.mova.utils.Constants
import com.example.mova.utils.Resource

private const val TAG = "TvDetailsFra mohamed"

class TvShowDetailsFragment : Fragment() {

    private lateinit var castAdapter: CastAdapter
    private lateinit var crewAdapter: CrewAdapter
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var similarTvShowAdapter: SimilarTvShowAdapter
    private lateinit var recommendedTvShowAdapter: RecommendedTvShowAdapter
    private lateinit var productionCompanyAdapter: ProductionCompanyAdapter
    private lateinit var seasonsAdapter: SeasonsAdapter
    private lateinit var viewModel: MoviesViewModel
    private var tvId = -1
    private val args: TvShowDetailsFragmentArgs by navArgs()


    private lateinit var binding: FragmentTvShowDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvShowDetailsBinding.inflate(layoutInflater, container, false)

        setupCast()
        setupCrew()
        setupProductionCompaniesAdapter()
        setupRecommendedMovies()
        setupSeasonsAdapter()
        setupSimilarMovies()
        setupGenreAdapter()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel
        tvId = args.tvId
        val tvExtra = args.tv
        if (tvId != -1) {
            viewModel.getTvShow(tvId)
            viewModel.getRecommendedTvShows(tvId)
            viewModel.getSimilarTvShows(tvId)
            viewModel.getTvShowCredits(tvId)
            viewModel.getTvTrailer(tvId)

            var saveTv: Tv? = null
            viewModel.getSavedTv(tvId).observe(viewLifecycleOwner) { tv ->
                if (tv == null) {
                    binding.save.apply {
                        setImageResource(R.drawable.ic_save)
                        setOnClickListener {
                            viewModel.insertTv(saveTv!!)
                        }
                    }
                } else {
                    binding.save.apply {
                        setImageResource(R.drawable.ic_saved)
                        setOnClickListener {
                            viewModel.deleteTv(tv)
                        }
                    }
                }
            }

            viewModel.tvTrailer.observe(viewLifecycleOwner) { response ->
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

                                binding.btnTrailer.setOnClickListener {
                                    val extra: String = if (s.isNotEmpty()) {
                                        s[0].key
                                    } else
                                        trailerResponse.results[0].key

                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            TrailerActivity::class.java
                                        ).putExtra(Constants.EXTRA_TRAILER, extra)
                                    )
                                }
                            } else {
                                binding.btnTrailer.setOnClickListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Un Available",
                                        Toast.LENGTH_SHORT
                                    ).show()
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

            viewModel.tvShow.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        Log.d(TAG, "tvShow details observe: loading")
                        loadingDetails(true)
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "tvShow details observe :success")
                        loadingDetails(false)
                        response.data?.let { tvShow ->
                            saveTv = tvShow
                            binding.tvOverView.text = tvShow.overview
                            binding.tvName.text = tvShow.name
                            genreAdapter.differ.submitList(tvShow.genres)
                            var spokenLanguage = ""
                            for (i in tvShow.spoken_languages.indices)
                                spokenLanguage += "${tvShow.spoken_languages[i].name}\n"
                            binding.tvSpokenLang.text = spokenLanguage
                            var productionCountries = ""
                            for (i in tvShow.production_countries.indices)
                                productionCountries += "${tvShow.production_countries[i].name}\n"
                            binding.tvSpokenLang.text = spokenLanguage
                            binding.tvProductionCountries.text = productionCountries
                            binding.tvName.isSelected = true
                            binding.tvSeason.text = tvShow.number_of_seasons.toString()
                            binding.tvRating.text = String.format("%.01f", tvShow.vote_average)
                            binding.tvStatus.text = tvShow.status

                            tvShow.poster_path?.let {
                                Glide.with(requireContext()).load(Constants.IMAGE_URL + it)
                                    .into(binding.imageView)
                                Glide.with(requireContext()).load(Constants.IMAGE_URL + it)
                                    .into(binding.imageViewTrailer)
                            } ?: run {
                                Glide.with(requireContext()).load(R.drawable.series)
                                    .into(binding.imageView)
                                Glide.with(requireContext()).load(R.drawable.series)
                                    .into(binding.imageViewTrailer)
                            }
                            seasonsAdapter.differ.submitList(tvShow.seasons)
                            productionCompanyAdapter.differ.submitList(tvShow.production_companies)
                        }

                    }
                    is Resource.Error -> {
                        Log.e(TAG, "tvShow details observe : ${response.message}")
                        loadingDetails(false)
                    }
                }
            }

            viewModel.similarTvShow.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        Log.d(TAG, "similar tvShow observe: loading")
                        loadingSimilar(true)
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "similar tvShow observe :success")
                        loadingSimilar(false)
                        response.data?.let { list ->
                            similarTvShowAdapter.differ.submitList(list.results)
                        }

                    }
                    is Resource.Error -> {
                        Log.e(TAG, "similar tvShow observe : ${response.message}")
                        loadingSimilar(false)
                    }
                }
            }

            viewModel.recommendedTvShow.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        Log.d(TAG, "recommended tvShow observe: loading")
                        loadingRecommending(true)
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "recommended tvShow observe :success")
                        response.data?.let { list ->
                            recommendedTvShowAdapter.differ.submitList(list.results)
                            loadingRecommending(false)
                        }

                    }
                    is Resource.Error -> {
                        Log.e(TAG, "recommended movies observe : ${response.message}")
                        loadingRecommending(false)
                    }
                }

            }

            viewModel.tvShowCredits.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        Log.d(TAG, "tvShow credits observe: loading")
                        loadingCredits(true)
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "tvShow credits observe :success")
                        loadingCredits(false)
                        response.data?.let { list ->
                            castAdapter.differ.submitList(list.cast)
                            crewAdapter.differ.submitList(list.crew)
                        }

                    }
                    is Resource.Error -> {
                        Log.e(TAG, "tvShow credits observe : ${response.message}")
                        loadingCredits(false)
                    }
                }
            }
        } else {  // saved
            tvExtra.let { tvShow ->
                binding.tvOverView.text = tvShow?.overview
                binding.tvName.text = tvShow?.name
                binding.tvName.isSelected = true
                binding.tvSeason.text = tvShow?.number_of_seasons.toString()
                binding.tvRating.text = tvShow?.vote_average.toString()
                tvShow?.poster_path?.let {
                    Glide.with(requireContext()).load(Constants.IMAGE_URL + it)
                        .into(binding.imageView)
                } ?: Glide.with(requireContext()).load(R.drawable.series)
                    .into(binding.imageView)
                seasonsAdapter.differ.submitList(tvShow?.seasons)
                productionCompanyAdapter.differ.submitList(tvShow?.production_companies)
            }
        }


    }

    private fun loadingCredits(loading: Boolean) {
        if (loading) {
            binding.shimmerCast.visibility = View.VISIBLE
            binding.shimmerCrew.visibility = View.VISIBLE
            binding.castRecyclerView.visibility = View.INVISIBLE
            binding.crewRecyclerView.visibility = View.INVISIBLE
        } else {
            binding.shimmerCast.visibility = View.INVISIBLE
            binding.shimmerCrew.visibility = View.INVISIBLE
            binding.castRecyclerView.visibility = View.VISIBLE
            binding.crewRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun loadingSimilar(loading: Boolean) {
        if (loading) {
            binding.shimmerSimilarMovies.visibility = View.VISIBLE
            binding.similarRecyclerView.visibility = View.INVISIBLE
        } else {
            binding.shimmerSimilarMovies.visibility = View.INVISIBLE
            binding.similarRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun loadingRecommending(loading: Boolean) {
        if (loading) {
            binding.shimmerRecommendedMovies.visibility = View.VISIBLE
            binding.recommendedRecyclerView.visibility = View.INVISIBLE
        } else {
            binding.recommendedRecyclerView.visibility = View.VISIBLE
            binding.shimmerRecommendedMovies.visibility = View.INVISIBLE
        }
    }

    private fun loadingDetails(loading: Boolean) {
        if (loading) {
            binding.imageView.visibility = View.INVISIBLE
            binding.tvOverView.visibility = View.INVISIBLE
            binding.shimmerImageView.visibility = View.VISIBLE
            binding.shimmerOverView.visibility = View.VISIBLE
            binding.shimmerSeasons.visibility = View.VISIBLE
            binding.seasonsRecyclerView.visibility = View.INVISIBLE
            binding.productionCompaniesRecyclerView.visibility = View.INVISIBLE
            binding.shimmerProductionCompanies.visibility = View.VISIBLE
        } else {
            binding.imageView.visibility = View.VISIBLE
            binding.tvOverView.visibility = View.VISIBLE
            binding.shimmerImageView.visibility = View.INVISIBLE
            binding.shimmerOverView.visibility = View.INVISIBLE
            binding.shimmerSeasons.visibility = View.INVISIBLE
            binding.seasonsRecyclerView.visibility = View.VISIBLE
            binding.productionCompaniesRecyclerView.visibility = View.VISIBLE
            binding.shimmerProductionCompanies.visibility = View.INVISIBLE
        }
    }

    private fun setupCast() {
        castAdapter = CastAdapter(object : OnCastClickListener {
            override fun onClick(cast: Cast) {
                findNavController().navigate(
                    TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToPeopleFragment(
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
                    TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToPeopleFragment(
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
        recommendedTvShowAdapter = RecommendedTvShowAdapter(object : OnTvShowClickListener {
            override fun onClick(tv: Tv) {
                Log.d(TAG, "recommended onClick: $tv")
                loadTvShow(tv.id)
            }
        }, requireContext())
        binding.recommendedRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = recommendedTvShowAdapter
        }
    }

    private fun setupGenreAdapter() {
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            genreAdapter = GenreAdapter()
            adapter = genreAdapter
        }
    }

    private fun setupSimilarMovies() {
        similarTvShowAdapter = SimilarTvShowAdapter(object : OnTvShowClickListener {
            override fun onClick(tv: Tv) {
                Log.d(TAG, "similar onClick: $tv")
                loadTvShow(tv.id)
            }
        }, requireContext())
        binding.similarRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = similarTvShowAdapter
        }
    }

    private fun setupProductionCompaniesAdapter() {
        productionCompanyAdapter = ProductionCompanyAdapter(requireContext())
        binding.productionCompaniesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = productionCompanyAdapter
        }
    }

    private fun setupSeasonsAdapter() {
        seasonsAdapter = SeasonsAdapter(object : OnSeasonClickListener {
            override fun onClick(season: Season) {
                findNavController().navigate(
                    TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToSeasonFragment(
                        season.season_number,
                        tvId
                    )
                )
            }
        }, requireContext())
        binding.seasonsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = seasonsAdapter
        }
    }

    private fun loadTvShow(id: Int) {
        viewModel.getTvShow(id)
        viewModel.getTvShowCredits(id)
        viewModel.getSimilarTvShows(id)
        viewModel.getRecommendedTvShows(id)
        viewModel.getTvTrailer(id)

    }


}