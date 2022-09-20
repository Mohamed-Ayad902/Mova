package com.example.mova.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.adapters.CrewAdapter
import com.example.mova.adapters.GuestStarAdapter
import com.example.mova.adapters.callbacks.OnCrewClickListener
import com.example.mova.adapters.callbacks.OnGuestClickListener
import com.example.mova.data.movie.Crew
import com.example.mova.data.tv_season.GuestStar
import com.example.mova.databinding.FragmentEpisodeBinding
import com.example.mova.utils.Constants

private const val TAG = "EpisodeFragment mohamed"

class EpisodeFragment : Fragment() {

    private val args: EpisodeFragmentArgs by navArgs()
    private lateinit var crewAdapter: CrewAdapter
    private lateinit var guestsAdapter: GuestStarAdapter
    private lateinit var binding: FragmentEpisodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodeBinding.inflate(layoutInflater, container, false)
        setupGuestStarRecyclerView()
        setupCrewRecyclerView()
        binding.back.setOnClickListener { requireActivity().onBackPressed() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val episode = args.episode


        binding.apply {
            tvName.text = episode.name
            tvName.isSelected = true
            airDate.text = episode.air_date
            tvOverView.text = episode.overview
            Glide.with(requireContext())
                .load(Constants.IMAGE_URL + episode.still_path)
                .into(imageView)
        }

        guestsAdapter.differ.submitList(episode.guest_stars)
        crewAdapter.differ.submitList(episode.crew)

    }

    private fun setupGuestStarRecyclerView() {
        guestsAdapter = GuestStarAdapter(object : OnGuestClickListener {
            override fun onClick(guestStar: GuestStar) {
                findNavController().navigate(
                    EpisodeFragmentDirections.actionEpisodeFragmentToPeopleFragment(
                        guestStar.id
                    )
                )
            }
        }, requireContext())
        binding.guestStarRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = guestsAdapter
        }
    }

    private fun setupCrewRecyclerView() {
        crewAdapter = CrewAdapter(object : OnCrewClickListener {
            override fun onClick(crew: Crew) {
                findNavController().navigate(
                    EpisodeFragmentDirections.actionEpisodeFragmentToPeopleFragment(
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

}