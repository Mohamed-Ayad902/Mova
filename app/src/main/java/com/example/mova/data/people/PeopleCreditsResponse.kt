package com.example.mova.data.people

data class PeopleCreditsResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)