package com.example.mova.data.movie

import com.example.mova.data.Poster

data class MoviesTvImagesResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<Poster>
)