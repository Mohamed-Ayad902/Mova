package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnSavedMovieClickListener
import com.example.mova.data.movie.Movie
import com.example.mova.databinding.SavedItemBinding
import com.example.mova.utils.Constants

class SavedMoviesAdapter(
    private val listener: OnSavedMovieClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<SavedMoviesAdapter.SavedMoviesVH>() {

    inner class SavedMoviesVH(val binding: SavedItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.room_id == newItem.room_id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SavedMoviesVH(
            SavedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: SavedMoviesVH, position: Int) {
        val movie = differ.currentList[position]
        holder.binding.apply {
            movie.poster_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + movie.poster_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
            tvName.text = movie.original_title
            tvOverView.text = movie.overview
            tvName.isSelected = true
            ratingBar.rating = (movie.vote_average / 2).toFloat()
            tvRating.text = String.format("%.01f", movie.vote_average)
            save.setOnClickListener {
                listener.onClick(movie)
            }
            holder.itemView.setOnClickListener {
                listener.onClick(movie)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}