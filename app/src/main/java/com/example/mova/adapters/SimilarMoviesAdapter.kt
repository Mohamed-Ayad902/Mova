package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnMovieClickListener
import com.example.mova.data.movie.Movie
import com.example.mova.databinding.PopularMoviesItemBinding
import com.example.mova.utils.Constants

class SimilarMoviesAdapter(
    private val listener: OnMovieClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<SimilarMoviesAdapter.SimilarMoviesVH>() {

    inner class SimilarMoviesVH(val binding: PopularMoviesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SimilarMoviesVH(
            PopularMoviesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: SimilarMoviesVH, position: Int) {
        val movie = differ.currentList[position]
        holder.binding.apply {
            movie.poster_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + movie.poster_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
            popularMTitle.text = movie.title
            popularMTitle.isSelected = true
            ratingBar.rating = (movie.vote_average / 2).toFloat()
            tvRate.text = String.format("%.01f",movie.vote_average)
            holder.itemView.setOnClickListener {
                listener.onClick(movie)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}