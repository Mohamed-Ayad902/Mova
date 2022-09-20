package com.example.mova.utils

class Constants {

    companion object {
        const val ON_BOARDING_SHARED_PREF = "boarding"
        const val ON_BOARDING_FINISHED = "finished"
        const val MOVIES_API_KEY = "a48b60136610fe57f8026f78b7338bd9"
        const val YOUTUBE_API_KEY = "AIzaSyDLIab1DqNOiJoyzDp-X44XZoOPhWcRWOw"
        const val MOVIES_BASE_URL = "https://api.themoviedb.org/3/"
        const val QUOTES_BASE_URL = "https://movie-quote-api.herokuapp.com/v1/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/original"
        const val SEARCH_DELAY_TIME = 500L
        const val PAGE_SIZE = 20

        const val EXTRA_TRAILER = "EXTRA_TRAILER"

        const val DIR_TOP_MOVIES = 10
        const val DIR_POPULAR_MOVIES = 11
        const val DIR_NOW_PLAYING_MOVIES = 12
        const val DIR_UP_COMING_MOVIES = 13

        const val DIR_TOP_TV = 14
        const val DIR_POPULAR_TV = 15
        const val DIR_AIRING_TODAY_TV = 16
        const val DIR_ON_AIR_TV = 17
    }
}