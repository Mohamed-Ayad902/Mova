package com.example.mova.repository

import androidx.lifecycle.LiveData
import com.example.mova.data.ReviewsResponse
import com.example.mova.data.TrailerResponse
import com.example.mova.data.movie.*
import com.example.mova.data.people.People
import com.example.mova.data.people.PeopleCreditsResponse
import com.example.mova.data.people.PeopleResponse
import com.example.mova.data.quote.QuoteResponse
import com.example.mova.data.tv.Tv
import com.example.mova.data.tv.TvResponse
import com.example.mova.data.tv_season.*
import retrofit2.Response

interface MoviesRepository {

    suspend fun getMovie(movie_id: Int)
            : Response<Movie>

    suspend fun getMovieTrailer(movie_id: Int)
            : Response<TrailerResponse>

    suspend fun getMovieReviews(movie_id: Int, pageNr: Int = 1)
            : Response<ReviewsResponse>

    suspend fun getRecommendedMovies(movie_id: Int, pageNr: Int = 1)
            : Response<MoviesResponse>

    suspend fun getSimilarMovies(movie_id: Int, pageNr: Int = 1)
            : Response<MoviesResponse>

    suspend fun getMovieImages(movie_id: Int)
            : Response<MoviesTvImagesResponse>

    suspend fun getMovieCredits(movie_id: Int)
            : Response<CreditResponse>

    suspend fun getNowPlayingMovies(pageNr: Int = 1)
            : Response<MoviesResponseWithDate>

    suspend fun getPopularMovies(pageNr: Int = 1)
            : Response<MoviesResponse>

    suspend fun getTopRatedMovies(pageNr: Int = 1)
            : Response<MoviesResponse>

    suspend fun searchMovies(query: String, pageNr: Int = 1)
            : Response<MoviesResponse>

    suspend fun getUpcomingMovies(pageNr: Int = 1)
            : Response<MoviesResponseWithDate>

    // end of movies responses


    // start of tv episodes responses

    suspend fun getTv(tv_id: Int)
            : Response<Tv>

    suspend fun getTvTrailer(tv_id: Int)
            : Response<TrailerResponse>

    suspend fun getPopularTv(pageNr: Int = 1)
            : Response<TvResponse>

    suspend fun getRecommendedTvShows(tv_id: Int)
            : Response<TvResponse>

    suspend fun getSimilarTvShows(tv_id: Int)
            : Response<TvResponse>

    suspend fun getTvShowImages(tv_id: Int)
            : Response<MoviesTvImagesResponse>

    suspend fun getTvShowCredits(tv_id: Int)
            : Response<CreditResponse>

    suspend fun getTopRatedTv(pageNr: Int = 1)
            : Response<TvResponse>

    // these tv shows are playing today
    suspend fun getAiringTodayTv(pageNr: Int = 1)
            : Response<TvResponse>

    // these tc shows are currently on air ( available to watch )
    suspend fun getOnAirTv(pageNr: Int = 1)
            : Response<TvResponse>

    suspend fun getTvReviews(tv_id: Int, pageNr: Int = 1)
            : Response<ReviewsResponse>

    suspend fun searchTv(query: String, pageNr: Int = 1)
            : Response<TvResponse>

    // end of tv shows responses


    // start of people responses

    suspend fun getPeople(person_id: Int)
            : Response<People>

    suspend fun getPopularPeople(pageNr: Int = 1)
            : Response<PeopleResponse>

    suspend fun getPeopleTvCredits(
        person_id: Int,
    ): Response<PeopleCreditsResponse>

    suspend fun getPeopleMovieCredits(
        person_id: Int,
    ): Response<PeopleCreditsResponse>

    suspend fun searchPeople(query: String, pageNr: Int = 1)
            : Response<PeopleResponse>

    // end of people responses


    // start of tv seasons responses

    suspend fun getTvSeason(tv_id: Int, season_number: Int)
            : Response<SeasonResponse>

    suspend fun getTvSeasonImages(tv_id: Int, season_number: Int)
            : Response<ImageResponse>

    suspend fun getTvSeasonVideos(tv_id: Int, season_number: Int)
            : Response<VideoResponse>

    // end of tv seasons responses


    // start of tv episodes responses

    suspend fun getEpisode(tv_id: Int, season_number: Int, episode_number: Int)
            : Response<Episode>

    suspend fun getTvEpisodeImages(tv_id: Int, season_number: Int, episode_number: Int):
            Response<EpisodeImageResponse>

    suspend fun getEpisodeVideos(tv_id: Int, season_number: Int, episode_number: Int)
            : Response<VideoResponse>

    // end of tv episodes responses


    // start of local db


    suspend fun insertMovie(movie: Movie): Long

    fun getSavedMovie(id: Int): LiveData<Movie>

    suspend fun deleteMovie(movie: Movie)

    fun getAllSavedMovies(): LiveData<List<Movie>>

    suspend fun insertTv(tv: Tv): Long

    fun getSavedTv(id: Int): LiveData<Tv>

    suspend fun deleteTv(tv: Tv)

    fun getAllSavedTv(): LiveData<List<Tv>>

    suspend fun getRandomQuote(): Response<QuoteResponse>

    suspend fun getSlugQuote(show_slug: String): Response<QuoteResponse>
}