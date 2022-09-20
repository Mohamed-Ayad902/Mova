package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnKnownForClickListener
import com.example.mova.data.people.KnownFor
import com.example.mova.databinding.PopularMoviesItemBinding
import com.example.mova.utils.Constants

class KnownForAdapter(
    private val listener: OnKnownForClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<KnownForAdapter.KnownForVH>() {

    inner class KnownForVH(val binding: PopularMoviesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<KnownFor>() {
        override fun areItemsTheSame(oldItem: KnownFor, newItem: KnownFor) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: KnownFor, newItem: KnownFor) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        KnownForVH(
            PopularMoviesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: KnownForVH, position: Int) {
        val knownFor = differ.currentList[position]
        holder.binding.apply {
            knownFor.poster_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + knownFor.poster_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
            popularMTitle.text = knownFor.title
            popularMTitle.isSelected = true
            ratingBar.rating = (knownFor.vote_average / 2).toFloat()
            tvRate.text = String.format("%.01f", knownFor.vote_average)
            holder.itemView.setOnClickListener {
                listener.onClick(knownFor)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}