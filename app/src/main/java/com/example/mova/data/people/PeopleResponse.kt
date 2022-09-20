package com.example.mova.data.people

data class PeopleResponse(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)