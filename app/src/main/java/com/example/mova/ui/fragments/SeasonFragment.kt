package com.example.mova.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mova.adapters.EpisodeAdapter
import com.example.mova.adapters.callbacks.OnEpisodeClickListener
import com.example.mova.data.tv_season.Episode
import com.example.mova.databinding.FragmentSeasonBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Constants
import com.example.mova.utils.Resource

private const val TAG = "SeasonFragment mohamed"

class SeasonFragment : Fragment() {

    private val args: SeasonFragmentArgs by navArgs()
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var viewModel: MoviesViewModel
    private lateinit var binding: FragmentSeasonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeasonBinding.inflate(layoutInflater, container, false)
        binding.back.setOnClickListener { requireActivity().onBackPressed() }
        setupEpisodeRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvId = args.tvId
        val seasonId = args.seasonId

        viewModel = (activity as HomeActivity).viewModel

        Log.d(TAG, "season id and tv id is :$seasonId -- $tvId ")
        viewModel.getTvShowSeason(tvId, seasonId)
        viewModel.tvShowSeason.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "season observe loading")
                    loadingSeason(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "season observe success")
                    loadingSeason(false)
                    response.data?.let { season ->
                        binding.tvName.text = season.name
                        binding.tvOverView.text = season.overview
                        binding.airDate.text = season.air_date
                        episodeAdapter.differ.submitList(season.episodes)
                        season.poster_path?.let {
                            Glide.with(requireContext())
                                .load(Constants.IMAGE_URL + season.poster_path)
                                .into(binding.imageView)
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "season observe error ${response.message} ${response.data}")
                    loadingSeason(false)
                }
            }
        }


    }

    private fun setupEpisodeRecyclerView() {
        episodeAdapter = EpisodeAdapter(object : OnEpisodeClickListener {
            override fun onClick(episode: Episode) {
                findNavController().navigate(
                    SeasonFragmentDirections.actionSeasonFragmentToEpisodeFragment(
                        episode
                    )
                )
            }
        }, requireContext())
        binding.episodesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = episodeAdapter
        }
    }

    private fun loadingSeason(loading: Boolean) {
        if (loading) {
            binding.imageView.visibility = View.INVISIBLE
            binding.tvOverView.visibility = View.INVISIBLE
            binding.airDate.visibility = View.INVISIBLE
            binding.episodesRecyclerView.visibility = View.INVISIBLE
            binding.tvName.visibility = View.INVISIBLE
            binding.shimmerImageView.visibility = View.VISIBLE
            binding.shimmerEpisodes.visibility = View.VISIBLE
            binding.shimmerTvName.visibility = View.VISIBLE
            binding.shimmerOverView.visibility = View.VISIBLE
        } else {
            binding.imageView.visibility = View.VISIBLE
            binding.tvOverView.visibility = View.VISIBLE
            binding.airDate.visibility = View.VISIBLE
            binding.episodesRecyclerView.visibility = View.VISIBLE
            binding.tvName.visibility = View.VISIBLE
            binding.shimmerImageView.visibility = View.INVISIBLE
            binding.shimmerEpisodes.visibility = View.INVISIBLE
            binding.shimmerTvName.visibility = View.INVISIBLE
            binding.shimmerOverView.visibility = View.INVISIBLE
        }
    }

}