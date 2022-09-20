package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnSavedTvClickListener
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.SavedItemBinding
import com.example.mova.utils.Constants

class SavedTvAdapter(
    private val listener: OnSavedTvClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<SavedTvAdapter.SavedTvVH>() {

    inner class SavedTvVH(val binding: SavedItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Tv>() {
        override fun areItemsTheSame(oldItem: Tv, newItem: Tv) =
            oldItem.room_id == newItem.room_id

        override fun areContentsTheSame(oldItem: Tv, newItem: Tv) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SavedTvVH(
            SavedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: SavedTvVH, position: Int) {
        val tv = differ.currentList[position]
        holder.binding.apply {
            tv.poster_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + tv.poster_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
            tvName.text = tv.original_name
            tvOverView.text = tv.overview
            tvName.isSelected = true
            ratingBar.rating = (tv.vote_average / 2).toFloat()
            tvRating.text = String.format("%.01f", tv.vote_average)
            save.setOnClickListener {
                listener.onClick(tv)
            }
            holder.itemView.setOnClickListener {
                listener.onClick(tv)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}