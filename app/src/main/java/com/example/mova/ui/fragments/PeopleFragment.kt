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
import com.example.mova.adapters.callbacks.OnPeopleCastCreditsClickListener
import com.example.mova.adapters.PeopleMovieCreditsAdapter
import com.example.mova.adapters.PeopleTvCreditsAdapter
import com.example.mova.data.people.Cast
import com.example.mova.data.people.People
import com.example.mova.databinding.FragmentPeopleBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Constants
import com.example.mova.utils.Resource

private const val TAG = "PeopleFragment mohamed"

class PeopleFragment : Fragment() {

    private val args: PeopleFragmentArgs by navArgs()
    private lateinit var viewModel: MoviesViewModel
    private lateinit var tvShowAdapter: PeopleTvCreditsAdapter
    private lateinit var movieAdapter: PeopleMovieCreditsAdapter

    private lateinit var binding: FragmentPeopleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(layoutInflater, container, false)
        setupTvRecyclerView()
        setupMovieRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel

        val peopleId = args.id

        viewModel.getPeople(peopleId)
        viewModel.getPeopleMovieCredits(peopleId)
        viewModel.getPeopleTvCredits(peopleId)

        viewModel.people.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "people observe loading: ")
                    loadingPeople(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "people observe success: ")
                    loadingPeople(false)
                    response.data?.let {
                        bindPeopleViews(it)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "people observe error: ${response.message} ")
                    loadingPeople(false)
                }
            }
        }

        viewModel.peopleMovieCredits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "people movie credits observe loading: ")
                }
                is Resource.Success -> {
                    Log.d(TAG, "people movie credits observe success: ")
                    response.data?.let {
                        movieAdapter.differ.submitList(it.cast)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "people movie credits observe error: ${response.message} ")
                }
            }
        }

        viewModel.peopleTvCredits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "people tv credits observe loading: ")
                }
                is Resource.Success -> {
                    Log.d(TAG, "people tv credits observe success: ")
                    response.data?.let {
                        tvShowAdapter.differ.submitList(it.cast)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "people tv credits observe error: ${response.message} ")
                }
            }
        }

    }

    private fun setupTvRecyclerView() {
        tvShowAdapter = PeopleTvCreditsAdapter(object : OnPeopleCastCreditsClickListener {
            override fun onClick(cast: Cast) {
                findNavController().navigate(
                    PeopleFragmentDirections.actionPeopleFragmentToTvShowDetailsFragment(
                        cast.id
                        ,null
                    )
                )
            }
        }, requireContext())
        binding.tvShowsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = tvShowAdapter
        }
    }

    private fun setupMovieRecyclerView() {
        movieAdapter = PeopleMovieCreditsAdapter(object : OnPeopleCastCreditsClickListener {
            override fun onClick(cast: Cast) {
                findNavController().navigate(
                    PeopleFragmentDirections.actionPeopleFragmentToMovieDetailsFragment(
                        cast.id
                        ,null
                    )
                )
            }
        }, requireContext())
        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = movieAdapter
        }
    }

    private fun bindPeopleViews(it: People) {
        binding.apply {
            if (it.deathday == null) {
                deathDay.visibility = View.GONE
                tvDeathDay.visibility = View.GONE
            } else {
                deathDay.visibility = View.VISIBLE
                tvDeathDay.visibility = View.VISIBLE
                tvDeathDay.text = it.deathday
            }
            it.profile_path.let {
                Glide.with(requireContext()).load(Constants.IMAGE_URL + it).into(imageView)
            }
            tvName.text = it.name
            tvOverView.text = it.biography
            knownFor.text = it.known_for_department
            tvBirthday.text = it.birthday
            popularity.text = String.format("%.01f", it.popularity)
        }
    }

    private fun loadingPeople(loading: Boolean) {
        binding.apply {
            if (loading) {
                shimmerBirthday.visibility = View.VISIBLE
                shimmerBirthday2.visibility = View.VISIBLE
                shimmerKnownFor.visibility = View.VISIBLE
                shimmerOverView.visibility = View.VISIBLE
                shimmerImageView.visibility = View.VISIBLE
                shimmerName.visibility = View.VISIBLE
                tvShows.visibility = View.INVISIBLE
                tvMovies.visibility = View.INVISIBLE
                birthday.visibility = View.INVISIBLE
                popularity.visibility = View.INVISIBLE
                knownFor.visibility = View.INVISIBLE
                tvName.visibility = View.INVISIBLE
                tvKnownFor.visibility = View.INVISIBLE
                imageView.visibility = View.INVISIBLE
            } else {
                shimmerBirthday.visibility = View.INVISIBLE
                shimmerBirthday2.visibility = View.INVISIBLE
                shimmerKnownFor.visibility = View.INVISIBLE
                shimmerOverView.visibility = View.INVISIBLE
                shimmerImageView.visibility = View.INVISIBLE
                shimmerName.visibility = View.INVISIBLE
                tvShows.visibility = View.VISIBLE
                tvMovies.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                tvName.visibility = View.VISIBLE
                birthday.visibility = View.VISIBLE
                popularity.visibility = View.VISIBLE
                knownFor.visibility = View.VISIBLE
                tvKnownFor.visibility = View.VISIBLE
            }
        }
    }

}