package com.example.mova.data.tv

data class TvResponse(
    val page: Int,
    val results: MutableList<Tv>,  // changed from list to shuffle it
    val total_results: Int,
    val total_pages: Int
)
