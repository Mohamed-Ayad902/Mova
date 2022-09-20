package com.example.mova.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.GenreAdapter
import com.example.mova.data.movie.Movie
import com.example.mova.databinding.FragmentSavedMovieBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.example.mova.utils.Constants
import com.google.android.material.snackbar.Snackbar

private const val TAG = "SavedMovieFrt mohamed"

class SavedMovieFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var genreAdapter: GenreAdapter
    private val args: SavedMovieFragmentArgs by navArgs()

    private lateinit var binding: FragmentSavedMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedMovieBinding.inflate(layoutInflater, container, false)
        setupGenreAdapter()
        return binding.root
    }

    private fun setupGenreAdapter() {
        binding.categoryRecyclerView.apply {
            genreAdapter = GenreAdapter()
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel

        val movie = args.movie

        setViews(movie)

        var saved = true
        binding.save.setOnClickListener {
            if (saved) {
                binding.save.setImageResource(R.drawable.ic_save)
                viewModel.deleteMovie(movie)
                Snackbar.make(requireView(), "Unsaved", Snackbar.LENGTH_SHORT).setAction("Undo") {
                    viewModel.insertMovie(movie)
                }.show()
            } else {
                binding.save.setImageResource(R.drawable.ic_saved)
                viewModel.insertMovie(movie)
                Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
            }
            saved = !saved
        }

    }

    private fun setViews(movie: Movie) {
        binding.save.setImageResource(R.drawable.ic_saved)
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
            Glide.with(requireContext()).load(Constants.IMAGE_URL + it)
                .into(binding.imageView)
        } ?: Glide.with(requireContext()).load(R.drawable.series)
            .into(binding.imageView)
        if (movie.adult)
            binding.tvAdult.text = "18+"
        else
            binding.tvAdult.text = "18-"

    }

}