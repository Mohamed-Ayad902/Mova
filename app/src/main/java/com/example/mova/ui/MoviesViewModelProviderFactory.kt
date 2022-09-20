package com.example.mova.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mova.repository.MovieRepositoryImpl

class MoviesViewModelProviderFactory(
    private val application: Application,
    private val repository: MovieRepositoryImpl
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesViewModel(application, repository) as T
    }

}