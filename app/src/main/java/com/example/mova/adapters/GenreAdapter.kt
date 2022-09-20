package com.example.mova.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mova.data.Genre
import com.example.mova.databinding.CategoryItemBinding

class GenreAdapter :
    RecyclerView.Adapter<GenreAdapter.CastVH>() {

    inner class CastVH(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CastVH(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: CastVH, position: Int) {
        val genre = differ.currentList[position]
        holder.binding.apply {
            tvCategory.text = genre.name
        }
    }

    override fun getItemCount() = differ.currentList.size


}