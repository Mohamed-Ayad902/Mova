package com.example.mova.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mova.adapters.SavedMoviesAdapter
import com.example.mova.adapters.SavedTvAdapter
import com.example.mova.adapters.callbacks.OnSavedMovieClickListener
import com.example.mova.adapters.callbacks.OnSavedTvClickListener
import com.example.mova.data.movie.Movie
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.FragmentSavedBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel


class SavedFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesAdapter: SavedMoviesAdapter
    private lateinit var tvAdapter: SavedTvAdapter
    private lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(layoutInflater, container, false)
        setupMoviesRecyclerView()
        setupTvRecyclerView()
        return binding.root
    }

    private fun setupTvRecyclerView() {
        tvAdapter = SavedTvAdapter(object : OnSavedTvClickListener {
            override fun onClick(tv: Tv) {
                Log.d("mohamed", "onClick:$tv ")
                findNavController().navigate(
                    SavedFragmentDirections.actionSavedFragmentToTvShowDetailsFragment(
                        -1,
                        tv
                    )
                )
            }

            override fun onSavedClick(tv: Tv) {

            }
        }, requireContext())
        binding.tvRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tvAdapter
        }
    }

    private fun setupMoviesRecyclerView() {
        moviesAdapter = SavedMoviesAdapter(object : OnSavedMovieClickListener {
            override fun onClick(movie: Movie) {
                findNavController().navigate(
                    SavedFragmentDirections.actionSavedFragmentToSavedMovieFragment(
                        movie
                    )
                )
            }

            override fun onSavedClick(movie: Movie) {

            }
        }, requireContext())
        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel

        viewModel.getAllSavedMovies().observe(viewLifecycleOwner) { list ->
            list.let {
                moviesAdapter.differ.submitList(it)
            }
        }

        viewModel.getAllSavedTv().observe(viewLifecycleOwner) { list ->
            list.let {
                tvAdapter.differ.submitList(it)
            }

        }

    }

}