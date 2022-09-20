package com.example.mova.repository

import com.example.mova.api.QuotesRetrofitInstance
import com.example.mova.api.RetrofitInstance
import com.example.mova.data.movie.Movie
import com.example.mova.data.tv.Tv
import com.example.mova.db.SavedDao

class MovieRepositoryImpl(private val dao: SavedDao) : MoviesRepository {

    override suspend fun getMovie(movie_id: Int) =
        RetrofitInstance.api.getMovie(movie_id = movie_id)

    override suspend fun getMovieTrailer(movie_id: Int) =
        RetrofitInstance.api.getMovieTrailer(movie_id = movie_id)

    override suspend fun getTvTrailer(tv_id: Int) =
        RetrofitInstance.api.getTvTrailer(tv_id = tv_id)

    override suspend fun getMovieReviews(movie_id: Int, pageNr: Int) =
        RetrofitInstance.api.getMovieReviews(movie_id = movie_id, pageNr = pageNr)

    override suspend fun getRecommendedMovies(movie_id: Int, pageNr: Int) =
        RetrofitInstance.api.getRecommendedMovies(movie_id = movie_id, pageNr = pageNr)

    override suspend fun getSimilarMovies(movie_id: Int, pageNr: Int) =
        RetrofitInstance.api.getSimilarMovies(movie_id = movie_id, pageNr = pageNr)

    override suspend fun getMovieImages(movie_id: Int) =
        RetrofitInstance.api.getMovieImages(movie_id = movie_id)

    override suspend fun getMovieCredits(movie_id: Int) =
        RetrofitInstance.api.getMovieCredits(movie_id = movie_id)

    override suspend fun getNowPlayingMovies(pageNr: Int) =
        RetrofitInstance.api.getNowPlayingMovies(pageNr = pageNr)

    override suspend fun getPopularMovies(pageNr: Int) =
        RetrofitInstance.api.getPopularMovies(pageNr = pageNr)

    override suspend fun getTopRatedMovies(pageNr: Int) =
        RetrofitInstance.api.getTopRatedMovies(pageNr = pageNr)

    override suspend fun searchMovies(query: String, pageNr: Int) =
        RetrofitInstance.api.searchMovies(query = query, pageNr = pageNr)

    override suspend fun getUpcomingMovies(pageNr: Int) =
        RetrofitInstance.api.getUpcomingMovies(pageNr = pageNr)

    // end of movies responses


    // start of tv episodes responses

    override suspend fun getTv(tv_id: Int) = RetrofitInstance.api.getTv(tv_id = tv_id)

    override suspend fun getPopularTv(pageNr: Int) =
        RetrofitInstance.api.getPopularTv(pageNr = pageNr)

    override suspend fun getRecommendedTvShows(tv_id: Int) =
        RetrofitInstance.api.getRecommendedTvShows(tv_id = tv_id)

    override suspend fun getSimilarTvShows(tv_id: Int) =
        RetrofitInstance.api.getSimilarTvShows(tv_id = tv_id)

    override suspend fun getTvShowImages(tv_id: Int) =
        RetrofitInstance.api.getTvShowImages(tv_id = tv_id)

    override suspend fun getTvShowCredits(tv_id: Int) =
        RetrofitInstance.api.getTvShowCredits(tv_id = tv_id)

    override suspend fun getTopRatedTv(pageNr: Int) =
        RetrofitInstance.api.getTopRatedTv(pageNr = pageNr)

    // these tv shows are playing today
    override suspend fun getAiringTodayTv(pageNr: Int) =
        RetrofitInstance.api.getAiringTodayTv(pageNr = pageNr)

    // these tc shows are currently on air ( available to watch )
    override suspend fun getOnAirTv(pageNr: Int) = RetrofitInstance.api.getOnAirTv(pageNr = pageNr)

    override suspend fun getTvReviews(tv_id: Int, pageNr: Int) =
        RetrofitInstance.api.getTvReviews(tv_id = tv_id, pageNr = pageNr)

    override suspend fun searchTv(query: String, pageNr: Int) =
        RetrofitInstance.api.searchTv(query = query, pageNr = pageNr)

    // end of tv shows responses


    // start of people responses

    override suspend fun getPeople(person_id: Int) =
        RetrofitInstance.api.getPeople(person_id = person_id)

    override suspend fun getPopularPeople(pageNr: Int) =
        RetrofitInstance.api.getPopularPeople(pageNr = pageNr)

    override suspend fun getPeopleMovieCredits(person_id: Int) =
        RetrofitInstance.api.getPeopleMovieCredits(person_id = person_id)

    override suspend fun getPeopleTvCredits(person_id: Int) =
        RetrofitInstance.api.getPeopleTvCredits(person_id = person_id)

    override suspend fun searchPeople(query: String, pageNr: Int) =
        RetrofitInstance.api.searchPeople(query = query, pageNr = pageNr)

    // end of people responses


    // start of tv seasons responses

    override suspend fun getTvSeason(tv_id: Int, season_number: Int) =
        RetrofitInstance.api.getTvSeason(tv_id = tv_id, season_number = season_number)

    override suspend fun getTvSeasonImages(tv_id: Int, season_number: Int) =
        RetrofitInstance.api.getTvSeasonImages(tv_id = tv_id, season_number = season_number)

    override suspend fun getTvSeasonVideos(tv_id: Int, season_number: Int) =
        RetrofitInstance.api.getTvSeasonVideos(tv_id = tv_id, season_number = season_number)

    // end of tv seasons responses


    // start of tv episodes responses

    override suspend fun getEpisode(tv_id: Int, season_number: Int, episode_number: Int) =
        RetrofitInstance.api.getEpisode(
            tv_id = tv_id,
            season_number = season_number,
            episode_number = episode_number
        )

    override suspend fun getTvEpisodeImages(tv_id: Int, season_number: Int, episode_number: Int) =
        RetrofitInstance.api.getTvEpisodeImages(
            tv_id = tv_id,
            season_number = season_number,
            episode_number = episode_number
        )

    override suspend fun getEpisodeVideos(tv_id: Int, season_number: Int, episode_number: Int) =
        RetrofitInstance.api.getEpisodeVideos(
            tv_id = tv_id,
            season_number = season_number,
            episode_number = episode_number
        )

    // end of tv episodes responses


    // start of local db

    override suspend fun insertMovie(movie: Movie) = dao.insertMovie(movie)

    override fun getSavedMovie(id: Int) = dao.getSavedMovie(id)

    override suspend fun deleteMovie(movie: Movie) = dao.deleteMovie(movie)

    override fun getAllSavedMovies() = dao.getSavedMovies()

    override suspend fun insertTv(tv: Tv) = dao.insertTv(tv)

    override fun getSavedTv(id: Int) = dao.getSavedTv(id)

    override suspend fun deleteTv(tv: Tv) = dao.deleteTv(tv)

    override fun getAllSavedTv() = dao.getSavedTvShows()


    override suspend fun getRandomQuote() = QuotesRetrofitInstance.api.getRandomQuote()

    override suspend fun getSlugQuote(show_slug: String) =
        QuotesRetrofitInstance.api.getSlugQuote(show_slug)

}