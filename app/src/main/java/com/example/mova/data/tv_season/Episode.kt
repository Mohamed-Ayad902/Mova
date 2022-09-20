package com.example.mova.data.tv_season

import com.example.mova.data.movie.Crew
import java.io.Serializable

data class Episode(
    val air_date: String,
    val episode_number: Int,
    val crew: List<Crew>,
    val guest_stars: List<GuestStar>,
    val id: Int,
    val name: String,
    val overview: String,
    val production_code: String,
    val season_number: Int,
    val still_path: String,
    val vote_average: Double,
    val vote_count: Int,
) : Serializable