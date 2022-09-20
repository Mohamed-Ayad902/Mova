package com.example.mova.data.movie

data class MoviesResponse(
    val page: Int,
    val results: MutableList<Movie>,    // was List<Movie> but i changed it to shuffle it
    val total_results: Int,
    val total_pages: Int
)