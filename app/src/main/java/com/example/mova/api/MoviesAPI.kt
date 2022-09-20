package com.example.mova.api

import com.example.mova.data.ReviewsResponse
import com.example.mova.data.TrailerResponse
import com.example.mova.data.movie.*
import com.example.mova.data.people.People
import com.example.mova.data.people.PeopleCreditsResponse
import com.example.mova.data.people.PeopleResponse
import com.example.mova.data.tv.Tv
import com.example.mova.data.tv.TvResponse
import com.example.mova.data.tv_season.*
import com.example.mova.utils.Constants.Companion.MOVIES_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<TrailerResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<Movie>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<ReviewsResponse>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendedMovies(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
    ): Response<MoviesTvImagesResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
    ): Response<CreditResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<MoviesResponseWithDate>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<MoviesResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("query") query: String,
        @Query("page") pageNr: Int = 1
    ): Response<MoviesResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<MoviesResponseWithDate>

    // end of movies responses


    // start of tv episodes responses

    @GET("tv/{tv_id}")
    suspend fun getTv(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<Tv>

    @GET("tv/{tv_id}/videos")
    suspend fun getTvTrailer(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<TrailerResponse>


    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<TvResponse>

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvShows(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<TvResponse>

    @GET("tv/{tv_id}/recommendations")
    suspend fun getRecommendedTvShows(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<TvResponse>

    @GET("tv/{tv_id}/images")
    suspend fun getTvShowImages(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<MoviesTvImagesResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCredits(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<CreditResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTv(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<TvResponse>

    // these tv shows are playing today
    @GET("tv/airing_today")
    suspend fun getAiringTodayTv(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<TvResponse>

    // these tc shows are currently on air ( available to watch )
    @GET("tv/on_the_air")
    suspend fun getOnAirTv(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<TvResponse>

    @GET("tv/{tv_id}")
    suspend fun getTvReviews(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<ReviewsResponse>

    @GET("search/tv")
    suspend fun searchTv(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("query") query: String,
        @Query("page") pageNr: Int = 1
    ): Response<TvResponse>

    // end of tv shows responses


    // start of people responses

    @GET("person/{person_id}")
    suspend fun getPeople(
        @Path("person_id") person_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<People>

    @GET("person/{person_id}/tv_credits")
    suspend fun getPeopleTvCredits(
        @Path("person_id") person_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<PeopleCreditsResponse>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPeopleMovieCredits(
        @Path("person_id") person_id: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<PeopleCreditsResponse>

    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("page") pageNr: Int = 1
    ): Response<PeopleResponse>

    @GET("search/person")
    suspend fun searchPeople(
        @Query("api_key") apiKey: String = MOVIES_API_KEY,
        @Query("query") query: String,
        @Query("page") pageNr: Int = 1
    ): Response<PeopleResponse>

    // end of people responses


    // start of tv seasons responses

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getTvSeason(
        @Path("tv_id") tv_id: Int,
        @Path("season_number") season_number: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<SeasonResponse>

    @GET("tv/{tv_id}/season/{season_number}/images")
    suspend fun getTvSeasonImages(
        @Path("tv_id") tv_id: Int,
        @Path("season_number") season_number: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<ImageResponse>

    @GET("tv/{tv_id}/season/{season_number}/videos")
    suspend fun getTvSeasonVideos(
        @Path("tv_id") tv_id: Int,
        @Path("season_number") season_number: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<VideoResponse>

    // end of tv seasons responses


    // start of tv episodes responses

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisode(
        @Path("tv_id") tv_id: Int,
        @Path("season_number") season_number: Int,
        @Path("episode_number") episode_number: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<Episode>

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/images")
    suspend fun getTvEpisodeImages(
        @Path("tv_id") tv_id: Int,
        @Path("season_number") season_number: Int,
        @Path("episode_number") episode_number: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<EpisodeImageResponse>

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/videos")
    suspend fun getEpisodeVideos(
        @Path("tv_id") tv_id: Int,
        @Path("season_number") season_number: Int,
        @Path("episode_number") episode_number: Int,
        @Query("api_key") apiKey: String = MOVIES_API_KEY
    ): Response<VideoResponse>

}