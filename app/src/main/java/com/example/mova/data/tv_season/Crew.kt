package com.example.mova.data.tv_season

data class Crew(
    val department: String,
    val job: String,
    val credit_id: String,
    val adult: Boolean?,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?,
)
