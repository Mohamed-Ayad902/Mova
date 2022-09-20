package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.adapters.callbacks.OnSeasonClickListener
import com.example.mova.data.tv.Season
import com.example.mova.databinding.SaesonItemBinding
import com.example.mova.utils.Constants

class SeasonsAdapter(
    private val listener: OnSeasonClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<SeasonsAdapter.SeasonVH>() {

    inner class SeasonVH(val binding: SaesonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Season>() {
        override fun areItemsTheSame(oldItem: Season, newItem: Season) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Season, newItem: Season) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SeasonVH(
            SaesonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SeasonVH, position: Int) {
        val season = differ.currentList[position]
        holder.binding.apply {
            season.poster_path.let {
                Glide.with(context).load(Constants.IMAGE_URL + season.poster_path)
                    .into(imageView)
            }
            tvSeasonName.text = season.name
            holder.itemView.setOnClickListener {
                listener.onClick(season)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}