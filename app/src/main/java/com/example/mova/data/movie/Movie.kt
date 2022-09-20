package com.example.mova.data.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mova.data.*
import java.io.Serializable

@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey(autoGenerate = true) val room_id: Int? = null,
    val adult: Boolean,
    val backdrop_path: String?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val imdb_id: String?,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
) : Serializable