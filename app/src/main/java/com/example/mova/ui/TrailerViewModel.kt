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
import com.example.mova.repository.MovieRepositoryImpl
import com.example.mova.utils.MoviesApplication
import com.example.mova.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Response
import java.io.IOException

class TrailerViewModel(
    application: Application,
    private val repository: MovieRepositoryImpl
) : AndroidViewModel(application) {

    private val _movieTrailer: MutableLiveData<Resource<TrailerResponse>> = MutableLiveData()
    val movieTrailer = _movieTrailer as LiveData<Resource<TrailerResponse>>


    fun getMovieTrailer(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieTrailerSafeCall(id)
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

    private fun handleMovieTrailerResponse(response: Response<TrailerResponse>): Resource<TrailerResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

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