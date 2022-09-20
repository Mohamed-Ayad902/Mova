package com.example.mova.data.people

import java.io.Serializable

data class Result(
    val adult: Boolean,
    val id: Int,
    val known_for: List<KnownFor>,
    val name: String,
    val popularity: Double,
    val profile_path: String?
) : Serializable