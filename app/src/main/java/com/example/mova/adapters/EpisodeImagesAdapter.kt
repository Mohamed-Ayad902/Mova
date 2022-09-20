package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.data.Poster
import com.example.mova.databinding.ImageItemBinding
import com.example.mova.utils.Constants

class EpisodeImagesAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<EpisodeImagesAdapter.EpisodeImagesVH>() {

    inner class EpisodeImagesVH(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Poster>() {
        override fun areItemsTheSame(oldItem: Poster, newItem: Poster) =
            oldItem.file_path == newItem.file_path

        override fun areContentsTheSame(oldItem: Poster, newItem: Poster) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeImagesVH(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: EpisodeImagesVH, position: Int) {
        val poster = differ.currentList[position]
        holder.binding.apply {
            Glide.with(context).load(Constants.IMAGE_URL + poster.file_path).into(imageView)
        }
    }

    override fun getItemCount() = differ.currentList.size


}