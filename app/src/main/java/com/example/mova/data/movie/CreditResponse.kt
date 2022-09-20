package com.example.mova.data.movie

data class CreditResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)