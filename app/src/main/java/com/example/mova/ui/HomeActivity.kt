package com.example.mova.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mova.databinding.ActivityHomeBinding
import com.example.mova.db.SavedDatabase
import com.example.mova.repository.MovieRepositoryImpl

class HomeActivity : AppCompatActivity() {

    lateinit var viewModel: MoviesViewModel
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = MovieRepositoryImpl(SavedDatabase.getInstance(this).dao())
        val viewModelProviderFactory = MoviesViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MoviesViewModel::class.java]

    }
}