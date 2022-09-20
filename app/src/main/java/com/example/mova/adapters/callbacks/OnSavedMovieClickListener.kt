package com.example.mova.adapters.callbacks

import com.example.mova.data.movie.Movie

interface OnSavedMovieClickListener {
    fun onClick(movie: Movie)
    fun onSavedClick(movie: Movie)
}