package com.example.mova.data.tv

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mova.data.Genre
import com.example.mova.data.ProductionCompany
import com.example.mova.data.ProductionCountry
import com.example.mova.data.SpokenLanguage
import java.io.Serializable

@Entity(tableName = "tv_table")
data class Tv(
    @PrimaryKey(autoGenerate = true) val room_id: Int,
    val backdrop_path: String?,
    val created_by: List<CreatedBy>,
    val episode_run_time: List<Int>,
    val first_air_date: String,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val name: String,
    val networks: List<Network>,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val seasons: List<Season>,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
) : Serializable