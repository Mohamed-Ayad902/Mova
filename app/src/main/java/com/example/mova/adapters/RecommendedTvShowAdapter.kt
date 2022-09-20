package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnTvShowClickListener
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.PopularMoviesItemBinding
import com.example.mova.utils.Constants

class RecommendedTvShowAdapter(
    private val listener: OnTvShowClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<RecommendedTvShowAdapter.RecommendedTvShowVH>() {

    inner class RecommendedTvShowVH(val binding: PopularMoviesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Tv>() {
        override fun areItemsTheSame(oldItem: Tv, newItem: Tv) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Tv, newItem: Tv) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecommendedTvShowVH(
            PopularMoviesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: RecommendedTvShowVH, position: Int) {
        val tv = differ.currentList[position]
        holder.binding.apply {
            tv.poster_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + tv.poster_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
            popularMTitle.text = tv.name
            popularMTitle.isSelected = true
            ratingBar.rating = (tv.vote_average / 2).toFloat()
            tvRate.text = String.format("%.01f",tv.vote_average)
            holder.itemView.setOnClickListener {
                listener.onClick(tv)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}