package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.adapters.callbacks.OnPeopleCastCreditsClickListener
import com.example.mova.data.people.Cast
import com.example.mova.databinding.PopularMoviesItemBinding
import com.example.mova.utils.Constants

class PeopleTvCreditsAdapter(
    private val listener: OnPeopleCastCreditsClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<PeopleTvCreditsAdapter.CreditsVH>() {

    inner class CreditsVH(val binding: PopularMoviesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CreditsVH(
            PopularMoviesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: CreditsVH, position: Int) {
        val cast = differ.currentList[position]
        holder.binding.apply {
            Glide.with(context).load(Constants.IMAGE_URL + cast.poster_path)
                .into(imageView)
            ratingBar.rating = (cast.vote_average / 2).toFloat()
            tvRate.text = String.format("%.01f", cast.vote_average / 2)
            popularMTitle.text = cast.original_title
            popularMTitle.isSelected = true
            holder.itemView.setOnClickListener {
                listener.onClick(cast)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}