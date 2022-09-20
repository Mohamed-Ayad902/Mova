package com.example.mova.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mova.data.TrailerResponse
import com.example.mova.data.movie.CreditResponse
import com.example.mova.data.movie.Movie
import com.example.mova.data.movie.MoviesResponse
import com.example.mova.data.movie.MoviesResponseWithDate
import com.example.mova.data.people.People
import com.example.mova.data.people.PeopleCreditsResponse
import com.example.mova.data.people.PeopleResponse
import com.example.mova.data.quote.QuoteResponse
import com.example.mova.data.tv.Tv
import com.example.mova.data.tv.TvResponse
import com.example.mova.data.tv_season.Episode
import com.example.mova.data.tv_season.SeasonResponse
import com.example.mova.repository.MovieRepositoryImpl
import com.example.mova.utils.MoviesApplication
import com.example.mova.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Response
import java.io.IOException

const val noInternet = "no internet connection"
const val networkFailure = "network failure"
const val conversionError = "conversion error"

class MoviesViewModel(
    application: Application,
    private val repository: MovieRepositoryImpl
) :
    AndroidViewModel(application) {

    private val _movie: MutableLiveData<Resource<Movie>> = MutableLiveData()
    val movie = _movie as LiveData<Resource<Movie>>

    private val _movieTrailer: MutableLiveData<Resource<TrailerResponse>> = MutableLiveData()
    val movieTrailer = _movieTrailer as LiveData<Resource<TrailerResponse>>

    private val _tvTrailer: MutableLiveData<Resource<TrailerResponse>> = MutableLiveData()
    val tvTrailer = _tvTrailer as LiveData<Resource<TrailerResponse>>

    private val _topRatedMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val topRatedMovies = _topRatedMovies as LiveData<Resource<MoviesResponse>>
    private var topRatedMoviesResponse: MoviesResponse? = null
    private var topRatedMoviesPage = 1

    private val _popularMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val popularMovies = _popularMovies as LiveData<Resource<MoviesResponse>>
    private var popularMoviesResponse: MoviesResponse? = null
    private var popularMoviesPage = 1

    private val _nowPlayingMovies: MutableLiveData<Resource<MoviesResponseWithDate>> =
        MutableLiveData()
    val nowPlayingMovies = _nowPlayingMovies as LiveData<Resource<MoviesResponseWithDate>>
    private var nowPlayingMoviesResponse: MoviesResponseWithDate? = null
    private var nowPlayingMoviesPage = 1

    private val _upComingMovies: MutableLiveData<Resource<MoviesResponseWithDate>> =
        MutableLiveData()
    val upComingMovies = _upComingMovies as LiveData<Resource<MoviesResponseWithDate>>
    private var upComingMoviesResponse: MoviesResponseWithDate? = null
    private var upComingMoviesPage = 1

    private val _similarMovies: MutableLiveData<Resource<MoviesResponse>> =
        MutableLiveData()
    val similarMovies = _similarMovies as LiveData<Resource<MoviesResponse>>
    private var similarMoviesPage = 1

    private val _recommendedMovies: MutableLiveData<Resource<MoviesResponse>> =
        MutableLiveData()
    val recommendedMovies = _recommendedMovies as LiveData<Resource<MoviesResponse>>
    private var recommendedMoviesPage = 1

    private val _movieCredits: MutableLiveData<Resource<CreditResponse>> = MutableLiveData()
    val movieCredits = _movieCredits as LiveData<Resource<CreditResponse>>

    private val _popularTvShow: MutableLiveData<Resource<TvResponse>> =
        MutableLiveData()
    val popularTvShow = _popularTvShow as LiveData<Resource<TvResponse>>
    private var popularTvShowResponse: TvResponse? = null
    private var popularTvShowPage = 1

    private val _tvShow: MutableLiveData<Resource<Tv>> = MutableLiveData()
    val tvShow = _tvShow as LiveData<Resource<Tv>>

    private val _tvShowCredits: MutableLiveData<Resource<CreditResponse>> = MutableLiveData()
    val tvShowCredits = _tvShowCredits as LiveData<Resource<CreditResponse>>

    private val _similarTvShow: MutableLiveData<Resource<TvResponse>> = MutableLiveData()
    val similarTvShow = _similarTvShow as LiveData<Resource<TvResponse>>

    private val _recommendedTvShow: MutableLiveData<Resource<TvResponse>> = MutableLiveData()
    val recommendedTvShow = _recommendedTvShow as LiveData<Resource<TvResponse>>

    private val _tvShowSeason: MutableLiveData<Resource<SeasonResponse>> = MutableLiveData()
    val tvShowSeason = _tvShowSeason as LiveData<Resource<SeasonResponse>>

    private val _topRatedTvShow: MutableLiveData<Resource<TvResponse>> =
        MutableLiveData()
    val topRatedTvShow = _topRatedTvShow as LiveData<Resource<TvResponse>>
    private var topRatedTvShowResponse: TvResponse? = null
    private var topRatedTvShowPage = 1

    private val _airingTodayTvShow: MutableLiveData<Resource<TvResponse>> =
        MutableLiveData()
    val airingTodayTvShow = _airingTodayTvShow as LiveData<Resource<TvResponse>>
    private var airingTodayTvShowResponse: TvResponse? = null
    private var airingTodayTvShowPage = 1

    private val _onAirTvShow: MutableLiveData<Resource<TvResponse>> =
        MutableLiveData()
    val onAirTvShow = _onAirTvShow as LiveData<Resource<TvResponse>>
    private var onAirTvShowResponse: TvResponse? = null
    private var onAirTvShowPage = 1

    private val _popularPeople: MutableLiveData<Resource<PeopleResponse>> =
        MutableLiveData()
    val popularPeople = _popularPeople as LiveData<Resource<PeopleResponse>>
    private var popularPeopleResponse: PeopleResponse? = null
    private var popularPeoplePage = 1

    private val _episode: MutableLiveData<Resource<Episode>> = MutableLiveData()
    val episode = _episode as LiveData<Resource<Episode>>

    private val _searchMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val searchMovies = _searchMovies as LiveData<Resource<MoviesResponse>>
    private var searchMoviesQuery = ""
    private var searchMoviesResponse: MoviesResponse? = null
    var searchMoviesPage = 1

    private val _searchSeries: MutableLiveData<Resource<TvResponse>> = MutableLiveData()
    val searchSeries = _searchSeries as LiveData<Resource<TvResponse>>
    private var searchSeriesResponse: TvResponse? = null
    private var searchSeriesQuery = ""
    var searchSeriesPage = 1

    private val _searchPeople: MutableLiveData<Resource<PeopleResponse>> = MutableLiveData()
    val searchPeople = _searchPeople as LiveData<Resource<PeopleResponse>>
    private var searchPeopleResponse: PeopleResponse? = null
    private var searchPeopleQuery = ""
    var searchPeoplePage = 1

    private val _people: MutableLiveData<Resource<People>> = MutableLiveData()
    val people = _people as LiveData<Resource<People>>

    private val _peopleMovieCredits: MutableLiveData<Resource<PeopleCreditsResponse>> =
        MutableLiveData()
    val peopleMovieCredits = _peopleMovieCredits as LiveData<Resource<PeopleCreditsResponse>>

    private val _peopleTvCredits: MutableLiveData<Resource<PeopleCreditsResponse>> =
        MutableLiveData()
    val peopleTvCredits = _peopleTvCredits as LiveData<Resource<PeopleCreditsResponse>>

    private val _quote: MutableLiveData<Resource<QuoteResponse>> = MutableLiveData()
    val quote = _quote as LiveData<Resource<QuoteResponse>>

    init {
        getTopRatedMovies()
        getPopularMovies()
        getNowPlayingMovies()
        getUpComingMovies()
        getPopularTvShow()
        getTopRatedTvShow()
        getAiringTodayTvShow()
        getOnAirTvShow()
        getPopularPeople()
    }

    fun insertMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
    }

    fun insertTv(tv: Tv) = viewModelScope.launch {
        repository.insertTv(tv)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }

    fun deleteTv(tv: Tv) = viewModelScope.launch {
        repository.deleteTv(tv)
    }

    fun getSavedTv(id: Int) = repository.getSavedTv(id)

    fun getSavedMovie(id: Int) = repository.getSavedMovie(id)

    fun getAllSavedMovies() =
        repository.getAllSavedMovies()

    fun getAllSavedTv() =
        repository.getAllSavedTv()


    fun getMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieSafeCall(id)
        }
    }

    fun getMovieTrailer(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieTrailerSafeCall(id)
        }
    }

    fun getTvTrailer(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tvTrailerSafeCall(id)
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            topRatedMoviesSafeCall()
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            popularMoviesSafeCall()
        }
    }

    fun getRecommendedMovies(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            recommendedMoviesSafeCall(id)
        }
    }

    fun getSimilarMovies(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            similarMoviesSafeCall(id)
        }
    }

    fun getMovieCredits(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieCreditsSafeCall(id)
        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            nowPlayingMoviesSafeCall()
        }
    }

    fun getUpComingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            upComingMoviesSafeCall()
        }
    }

    fun getPopularTvShow() {
        viewModelScope.launch(Dispatchers.IO) {
            popularTvShowSafeCall()
        }
    }

    fun getTvShow(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tvShowSafeCall(id)
        }
    }

    fun getSimilarTvShows(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            similarTvShowSafeCall(id)
        }
    }

    fun getTvShowCredits(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tvShowCreditsSafeCall(id)
        }
    }

    fun getRecommendedTvShows(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            recommendedTvShowsSafeCall(id)
        }
    }

    fun getTvShowSeason(tv_id: Int, season_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tvShowSeasonSafeCall(tv_id, season_id)
        }
    }

    fun getEpisode(tv_id: Int, season_id: Int, episode_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tvShowEpisodeSafeCall(tv_id, season_id, episode_id)
        }
    }

    fun getTopRatedTvShow() {
        viewModelScope.launch(Dispatchers.IO) {
            topRatedTvShowSafeCall()
        }
    }

    fun getAiringTodayTvShow() {
        viewModelScope.launch(Dispatchers.IO) {
            airingTodayTvShowSafeCall()
        }
    }

    fun getOnAirTvShow() {
        viewModelScope.launch(Dispatchers.IO) {
            onAirTvShowSafeCall()
        }
    }

    fun getPopularPeople() {
        viewModelScope.launch(Dispatchers.IO) {
            popularPeopleSafeCall()
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchMoviesSafeCall(query)
        }
    }

    fun searchSeries(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchSeriesSafeCall(query)
        }
    }

    fun searchPeople(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchPeopleSafeCall(query)
        }
    }

    fun getPeople(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getPeopleSafeCall(id)
        }
    }

    fun getPeopleMovieCredits(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getPeopleMovieCreditsSafeCall(id)
        }
    }

    fun getPeopleTvCredits(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getPeopleTvCreditsSafeCall(id)
        }
    }

    fun getQuote(showSlug: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            getQuoteSafeCall(showSlug)
        }
    }

    private suspend fun getQuoteSafeCall(showSlug: String?) {
        _quote.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response: Response<QuoteResponse> = if (showSlug != null)
                    repository.getSlugQuote(showSlug)
                else
                    repository.getRandomQuote()
                _quote.postValue(handleQuoteResponse(response))
            } else {
                _quote.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _quote.postValue(Resource.Error(networkFailure))
                is JSONException -> _quote.postValue(Resource.Error(conversionError))
                else -> _quote.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private fun handleQuoteResponse(response: Response<QuoteResponse>): Resource<QuoteResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun movieSafeCall(id: Int) {
        _movie.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getMovie(id)
                _movie.postValue(handleMovieDetailsResponse(response))
            } else {
                _movie.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _movie.postValue(Resource.Error(networkFailure))
                is JSONException -> _movie.postValue(Resource.Error(conversionError))
                else -> _movie.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun movieTrailerSafeCall(id: Int) {
        _movieTrailer.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getMovieTrailer(id)
                _movieTrailer.postValue(handleMovieTrailerResponse(response))
            } else {
                _movieTrailer.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _movieTrailer.postValue(Resource.Error(networkFailure))
                is JSONException -> _movieTrailer.postValue(Resource.Error(conversionError))
                else -> _movieTrailer.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun tvTrailerSafeCall(id: Int) {
        _tvTrailer.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getMovieTrailer(id)
                _tvTrailer.postValue(handleTvTrailerResponse(response))
            } else {
                _tvTrailer.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _tvTrailer.postValue(Resource.Error(networkFailure))
                is JSONException -> _tvTrailer.postValue(Resource.Error(conversionError))
                else -> _tvTrailer.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private fun handleMovieTrailerResponse(response: Response<TrailerResponse>): Resource<TrailerResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTvTrailerResponse(response: Response<TrailerResponse>): Resource<TrailerResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun topRatedMoviesSafeCall() {
        _topRatedMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getTopRatedMovies(pageNr = topRatedMoviesPage)
                _topRatedMovies.postValue(handleTopRatedMoviesResponse(response))
            } else {
                _topRatedMovies.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _topRatedMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _topRatedMovies.postValue(Resource.Error(conversionError))
                else -> _topRatedMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun popularMoviesSafeCall() {
        _popularMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getPopularMovies(pageNr = popularMoviesPage)
                _popularMovies.postValue(handlePopularMoviesResponse(response))
            } else {
                _popularMovies.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _popularMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _popularMovies.postValue(Resource.Error(conversionError))
                else -> _popularMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun recommendedMoviesSafeCall(id: Int) {
        _recommendedMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    repository.getRecommendedMovies(movie_id = id, pageNr = recommendedMoviesPage)
                _recommendedMovies.postValue(handleRecommendedMoviesResponse(response))
            } else {
                _recommendedMovies.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _recommendedMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _recommendedMovies.postValue(Resource.Error(conversionError))
                else -> _recommendedMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun movieCreditsSafeCall(id: Int) {
        _movieCredits.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    repository.getMovieCredits(movie_id = id)
                _movieCredits.postValue(handleMovieCreditsResponse(response))
            } else {
                _movieCredits.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _movieCredits.postValue(Resource.Error(networkFailure))
                is JSONException -> _movieCredits.postValue(Resource.Error(conversionError))
                else -> _movieCredits.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun similarMoviesSafeCall(id: Int) {
        _similarMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    repository.getSimilarMovies(movie_id = id, pageNr = similarMoviesPage)
                _similarMovies.postValue(handleSimilarMoviesResponse(response))
            } else {
                _similarMovies.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _similarMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _similarMovies.postValue(Resource.Error(conversionError))
                else -> _similarMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun nowPlayingMoviesSafeCall() {
        _nowPlayingMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getNowPlayingMovies(pageNr = nowPlayingMoviesPage)
                _nowPlayingMovies.postValue(handleNowPlayingMoviesResponse(response))
            } else {
                _nowPlayingMovies.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _nowPlayingMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _nowPlayingMovies.postValue(Resource.Error(conversionError))
                else -> _nowPlayingMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun upComingMoviesSafeCall() {
        _upComingMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getUpcomingMovies(pageNr = upComingMoviesPage)
                _upComingMovies.postValue(handleUpComingMoviesResponse(response))
            } else {
                _upComingMovies.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _upComingMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _upComingMovies.postValue(Resource.Error(conversionError))
                else -> _upComingMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun popularTvShowSafeCall() {
        _popularTvShow.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getPopularTv(pageNr = popularTvShowPage)
                _popularTvShow.postValue(handlePopularTvShowResponse(response))
            } else {
                _popularTvShow.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _upComingMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _upComingMovies.postValue(Resource.Error(conversionError))
                else -> _upComingMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun tvShowSafeCall(id: Int) {
        _tvShow.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getTv(tv_id = id)
                _tvShow.postValue(handleTvShowResponse(response))
            } else {
                _tvShow.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _tvShow.postValue(Resource.Error(networkFailure))
                is JSONException -> _tvShow.postValue(Resource.Error(conversionError))
                else -> _tvShow.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun similarTvShowSafeCall(id: Int) {
        _similarTvShow.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getSimilarTvShows(tv_id = id)
                _similarTvShow.postValue(handleSimilarTvShowResponse(response))
            } else {
                _similarTvShow.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _similarTvShow.postValue(Resource.Error(networkFailure))
                is JSONException -> _similarTvShow.postValue(Resource.Error(conversionError))
                else -> _similarTvShow.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun recommendedTvShowsSafeCall(id: Int) {
        _recommendedTvShow.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getRecommendedTvShows(tv_id = id)
                _recommendedTvShow.postValue(handleRecommendedTvShowResponse(response))
            } else {
                _recommendedTvShow.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _recommendedTvShow.postValue(Resource.Error(networkFailure))
                is JSONException -> _recommendedTvShow.postValue(Resource.Error(conversionError))
                else -> _recommendedTvShow.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun tvShowCreditsSafeCall(id: Int) {
        _tvShowCredits.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getTvShowCredits(tv_id = id)
                _tvShowCredits.postValue(handleTvShowCreditsResponse(response))
            } else {
                _tvShowCredits.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _tvShowCredits.postValue(Resource.Error(networkFailure))
                is JSONException -> _tvShowCredits.postValue(Resource.Error(conversionError))
                else -> _tvShowCredits.postValue(Resource.Error(t.message!!))
            }
        }
    }


    private suspend fun tvShowSeasonSafeCall(tv_id: Int, season_id: Int) {
        _tvShowSeason.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getTvSeason(tv_id = tv_id, season_number = season_id)
                _tvShowSeason.postValue(handleTvShowSeasonsResponse(response))
            } else {
                _tvShowSeason.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _tvShowSeason.postValue(Resource.Error(networkFailure))
                is JSONException -> _tvShowSeason.postValue(Resource.Error(conversionError))
                else -> _tvShowSeason.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun tvShowEpisodeSafeCall(tv_id: Int, season_id: Int, episode_id: Int) {
        _episode.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getEpisode(
                    tv_id = tv_id,
                    season_number = season_id,
                    episode_number = episode_id
                )
                _episode.postValue(handleTvShowEpisodeResponse(response))
            } else {
                _episode.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _episode.postValue(Resource.Error(networkFailure))
                is JSONException -> _episode.postValue(Resource.Error(conversionError))
                else -> _episode.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun topRatedTvShowSafeCall() {
        _topRatedTvShow.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getTopRatedTv(pageNr = topRatedTvShowPage)
                _topRatedTvShow.postValue(handleTopRatedTvShowResponse(response))
            } else {
                _topRatedTvShow.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _topRatedTvShow.postValue(Resource.Error(networkFailure))
                is JSONException -> _topRatedTvShow.postValue(Resource.Error(conversionError))
                else -> _topRatedTvShow.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun airingTodayTvShowSafeCall() {
        _airingTodayTvShow.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getAiringTodayTv(pageNr = airingTodayTvShowPage)
                _airingTodayTvShow.postValue(handleAiringTodayTvShowResponse(response))
            } else {
                _airingTodayTvShow.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _airingTodayTvShow.postValue(Resource.Error(networkFailure))
                is JSONException -> _airingTodayTvShow.postValue(Resource.Error(conversionError))
                else -> _airingTodayTvShow.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun onAirTvShowSafeCall() {
        _onAirTvShow.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getOnAirTv(pageNr = onAirTvShowPage)
                _onAirTvShow.postValue(handleOnAirTvShowResponse(response))
            } else {
                _onAirTvShow.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _onAirTvShow.postValue(Resource.Error(networkFailure))
                is JSONException -> _onAirTvShow.postValue(Resource.Error(conversionError))
                else -> _onAirTvShow.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun popularPeopleSafeCall() {
        _popularPeople.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getPopularPeople(pageNr = popularPeoplePage)
                _popularPeople.postValue(handlePopularPeopleResponse(response))
            } else {
                _popularPeople.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _popularPeople.postValue(Resource.Error(networkFailure))
                is JSONException -> _popularPeople.postValue(Resource.Error(conversionError))
                else -> _popularPeople.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun searchMoviesSafeCall(query: String) {
        _searchMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                if (query != searchMoviesQuery) {
                    val response = repository.searchMovies(query, 1)
                    _searchMovies.postValue(handleSearchMoviesResponse(query, response))
                } else {
                    val response = repository.searchMovies(query, searchMoviesPage)
                    _searchMovies.postValue(handleSearchMoviesResponse(query, response))
                }
            } else {
                _searchMovies.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchMovies.postValue(Resource.Error(networkFailure))
                is JSONException -> _searchMovies.postValue(Resource.Error(conversionError))
                else -> _searchMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }


    private suspend fun searchSeriesSafeCall(query: String) {
        _searchSeries.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                if (query != searchSeriesQuery) {
                    val response =
                        repository.searchTv(query, 1)
                    _searchSeries.postValue(handleSearchSeriesResponse(query, response))
                } else {
                    val response = repository.searchTv(query, searchSeriesPage)
                    _searchSeries.postValue(handleSearchSeriesResponse(query, response))
                }
            } else {
                _searchSeries.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchSeries.postValue(Resource.Error(networkFailure))
                is JSONException -> _searchSeries.postValue(Resource.Error(conversionError))
                else -> _searchSeries.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun searchPeopleSafeCall(query: String) {
        _searchPeople.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                if (query != searchPeopleQuery) {
                    val response =
                        repository.searchPeople(query, 1)
                    _searchPeople.postValue(handleSearchPeopleResponse(query, response))
                } else {
                    val response =
                        repository.searchPeople(query, searchPeoplePage)
                    _searchPeople.postValue(handleSearchPeopleResponse(query, response))
                }
            } else {
                _searchPeople.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchPeople.postValue(Resource.Error(networkFailure))
                is JSONException -> _searchPeople.postValue(Resource.Error(conversionError))
                else -> _searchPeople.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun getPeopleSafeCall(id: Int) {
        _people.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    repository.getPeople(id)
                _people.postValue(handlePeopleResponse(response))
            } else {
                _people.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _people.postValue(Resource.Error(networkFailure))
                is JSONException -> _people.postValue(Resource.Error(conversionError))
                else -> _people.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun getPeopleMovieCreditsSafeCall(id: Int) {
        _peopleMovieCredits.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    repository.getPeopleMovieCredits(id)
                _peopleMovieCredits.postValue(handlePeopleMovieCreditsResponse(response))
            } else {
                _peopleMovieCredits.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _peopleMovieCredits.postValue(Resource.Error(networkFailure))
                is JSONException -> _peopleMovieCredits.postValue(Resource.Error(conversionError))
                else -> _peopleMovieCredits.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private suspend fun getPeopleTvCreditsSafeCall(id: Int) {
        _peopleTvCredits.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    repository.getPeopleTvCredits(id)
                _peopleTvCredits.postValue(handlePeopleTvCreditsResponse(response))
            } else {
                _peopleTvCredits.postValue(Resource.Error(noInternet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _peopleTvCredits.postValue(Resource.Error(networkFailure))
                is JSONException -> _peopleTvCredits.postValue(Resource.Error(conversionError))
                else -> _peopleTvCredits.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private fun handleMovieDetailsResponse(response: Response<Movie>): Resource<Movie> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTopRatedMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                topRatedMoviesPage++
                if (topRatedMoviesResponse == null) {
                    topRatedMoviesResponse = it
                } else {
                    val oldResponse = topRatedMoviesResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(topRatedMoviesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePopularMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                popularMoviesPage++
                if (popularMoviesResponse == null) {
                    popularMoviesResponse = it
                } else {
                    val oldResponse = popularMoviesResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(popularMoviesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRecommendedMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleMovieCreditsResponse(response: Response<CreditResponse>): Resource<CreditResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSimilarMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleNowPlayingMoviesResponse(response: Response<MoviesResponseWithDate>): Resource<MoviesResponseWithDate> {
        if (response.isSuccessful) {
            response.body()?.let {
                nowPlayingMoviesPage++
                if (nowPlayingMoviesResponse == null) {
                    nowPlayingMoviesResponse = it
                } else {
                    val oldResponse = nowPlayingMoviesResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(nowPlayingMoviesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleUpComingMoviesResponse(response: Response<MoviesResponseWithDate>): Resource<MoviesResponseWithDate> {
        if (response.isSuccessful) {
            response.body()?.let {
                upComingMoviesPage++
                if (upComingMoviesResponse == null) {
                    upComingMoviesResponse = it
                } else {
                    val oldResponse = upComingMoviesResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(upComingMoviesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePopularTvShowResponse(response: Response<TvResponse>): Resource<TvResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                popularTvShowPage++
                if (popularTvShowResponse == null) {
                    popularTvShowResponse = it
                } else {
                    val oldResponse = popularTvShowResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(popularTvShowResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTvShowResponse(response: Response<Tv>): Resource<Tv> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSimilarTvShowResponse(response: Response<TvResponse>): Resource<TvResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRecommendedTvShowResponse(response: Response<TvResponse>): Resource<TvResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTvShowCreditsResponse(response: Response<CreditResponse>): Resource<CreditResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTvShowSeasonsResponse(response: Response<SeasonResponse>): Resource<SeasonResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTvShowEpisodeResponse(response: Response<Episode>): Resource<Episode> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleAiringTodayTvShowResponse(response: Response<TvResponse>): Resource<TvResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                airingTodayTvShowPage++
                if (airingTodayTvShowResponse == null) {
                    airingTodayTvShowResponse = it
                } else {
                    val oldResponse = airingTodayTvShowResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(airingTodayTvShowResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTopRatedTvShowResponse(response: Response<TvResponse>): Resource<TvResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                topRatedTvShowPage++
                if (topRatedTvShowResponse == null) {
                    topRatedTvShowResponse = it
                } else {
                    val oldResponse = topRatedTvShowResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(topRatedTvShowResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleOnAirTvShowResponse(response: Response<TvResponse>): Resource<TvResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                onAirTvShowPage++
                if (onAirTvShowResponse == null) {
                    onAirTvShowResponse = it
                } else {
                    val oldResponse = onAirTvShowResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(onAirTvShowResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePopularPeopleResponse(response: Response<PeopleResponse>): Resource<PeopleResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                popularPeoplePage++
                if (popularPeopleResponse == null) {
                    popularPeopleResponse = it
                } else {
                    val oldResponse = popularPeopleResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                return Resource.Success(popularPeopleResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchMoviesResponse(
        query: String,
        response: Response<MoviesResponse>
    ): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (query != searchMoviesQuery) {
                    searchMoviesQuery = query
                    return Resource.Success(it)
                }
                searchMoviesPage++
                if (searchMoviesResponse == null) {
                    searchMoviesResponse = it
                } else {
                    val oldResponse = searchMoviesResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                searchMoviesQuery = query
                return Resource.Success(searchMoviesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchSeriesResponse(
        query: String,
        response: Response<TvResponse>
    ): Resource<TvResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (query != searchSeriesQuery) {
                    searchSeriesQuery = query
                    return Resource.Success(it)
                }
                searchSeriesPage++
                if (searchSeriesResponse == null) {
                    searchSeriesResponse = it
                } else {
                    val oldResponse = searchSeriesResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                searchSeriesQuery = query
                return Resource.Success(searchSeriesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchPeopleResponse(
        query: String,
        response: Response<PeopleResponse>
    ): Resource<PeopleResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (query != searchPeopleQuery) {
                    searchPeopleQuery = query
                    return Resource.Success(it)
                }
                searchPeoplePage++
                if (searchPeopleResponse == null) {
                    searchPeopleResponse = it
                } else {
                    val oldResponse = searchPeopleResponse?.results
                    val newResponse = it.results
                    oldResponse?.addAll(newResponse)
                }
                searchPeopleQuery = query
                return Resource.Success(searchPeopleResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handlePeopleResponse(response: Response<People>): Resource<People> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePeopleMovieCreditsResponse(response: Response<PeopleCreditsResponse>): Resource<PeopleCreditsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePeopleTvCreditsResponse(response: Response<PeopleCreditsResponse>): Resource<PeopleCreditsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    // checking network connection
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MoviesApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}