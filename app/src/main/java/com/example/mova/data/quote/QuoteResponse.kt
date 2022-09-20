package com.example.mova.data.quote

data class QuoteResponse(
    val contain_adult_lang: Boolean,
    val quote: String,
    val role: String,
    val show: String
)