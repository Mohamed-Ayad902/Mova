package com.example.mova.api

import com.example.mova.data.quote.QuoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuotesApi {

    @GET("quote/")
    suspend fun getRandomQuote(): Response<QuoteResponse>

    @GET("shows/{show_slug}")
    suspend fun getSlugQuote(@Path("show_slug") show_slug: String): Response<QuoteResponse>
}