package com.example.mova.data.tv_season

data class GuestStar(
    val credit_id: String,
    val order: Int,
    val character: String,
    val adult: Boolean,
    val gender: Int?,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?,
)
