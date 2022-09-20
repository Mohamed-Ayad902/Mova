package com.example.mova.data.tv_season

data class SeasonResponse(
    val _id: String,
    val air_date: String,
    val episodes: List<Episode>,
    val name: String,
    val overview: String,
    val id: Int,
    val poster_path: String?,
    val season_number: Int,
)
