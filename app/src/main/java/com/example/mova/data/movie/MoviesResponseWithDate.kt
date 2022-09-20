package com.example.mova.data.movie

import com.example.mova.data.Dates

data class MoviesResponseWithDate(
    val page: Int,
    val results: MutableList<Movie>,    // changed from List<Movie> to shuffle it
    val dates: Dates,
    val total_results: Int,
    val total_pages: Int
)