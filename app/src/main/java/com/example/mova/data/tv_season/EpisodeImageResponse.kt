package com.example.mova.data.tv_season

import com.example.mova.data.Poster

data class EpisodeImageResponse(
    val id: Int,
    val stills: List<Poster>
)