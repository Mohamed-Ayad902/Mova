package com.example.mova.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mova.R
import com.example.mova.adapters.GenreAdapter
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.FragmentSavedTvShowBinding
import com.example.mova.ui.HomeActivity
import com.example.mova.ui.MoviesViewModel
import com.google.android.material.snackbar.Snackbar

class SavedTvShowFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var genreAdapter: GenreAdapter
    private val args: SavedTvShowFragmentArgs by navArgs()

    private lateinit var binding: FragmentSavedTvShowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedTvShowBinding.inflate(layoutInflater, container, false)
        setupGenreRecyclerView()
        return binding.root
    }

    private fun setupGenreRecyclerView() {
        binding.categoryRecyclerView.apply {
            genreAdapter = GenreAdapter()
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel
        val tv = args.tv

        setViews(tv)

        var saved = true
        binding.save.setOnClickListener {
            if (saved) {
                binding.save.setImageResource(R.drawable.ic_save)
                viewModel.deleteTv(tv)
                Snackbar.make(requireView(), "Unsaved", Snackbar.LENGTH_SHORT).setAction("Undo") {
                    viewModel.insertTv(tv)
                }.show()
            } else {
                binding.save.setImageResource(R.drawable.ic_saved)
                viewModel.insertTv(tv)
                Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
            }
            saved = !saved
        }


    }

    private fun setViews(tvShow: Tv) {
        genreAdapter.differ.submitList(tvShow.genres)
        binding.tvOverView.text = tvShow.overview
        binding.tvName.text = tvShow.name
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
        binding.tvRating.text = tvShow.vote_average.toString()
        binding.tvStatus.text = tvShow.status

    }


}