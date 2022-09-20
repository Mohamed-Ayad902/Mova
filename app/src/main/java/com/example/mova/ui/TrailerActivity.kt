package com.example.mova.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mova.databinding.ActivityTrailerBinding
import com.example.mova.utils.Constants
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class TrailerActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityTrailerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra(Constants.EXTRA_TRAILER)

        loadTrailer(key!!)

    }

    private fun loadTrailer(key: String) {
        binding.ytPlayer.initialize(
            Constants.YOUTUBE_API_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    youTubePlayer.loadVideo(key)
                    youTubePlayer.play()
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    Log.e("mohamed", "onInitializationFailure: $youTubeInitializationResult")
                    Toast.makeText(this@TrailerActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

}