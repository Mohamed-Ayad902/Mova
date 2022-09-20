package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.adapters.callbacks.OnEpisodeClickListener
import com.example.mova.data.tv_season.Episode
import com.example.mova.databinding.ProductionCompanyItemBinding
import com.example.mova.utils.Constants

class EpisodeAdapter(
    private val listener: OnEpisodeClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<EpisodeAdapter.EpisodeVH>() {

    inner class EpisodeVH(val binding: ProductionCompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeVH(
            ProductionCompanyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: EpisodeVH, position: Int) {
        val episode = differ.currentList[position]
        holder.binding.apply {
            episode.still_path.let {
                Glide.with(context).load(Constants.IMAGE_URL + episode.still_path)
                    .into(imageView)
            }
            tvCompanyName.text = episode.name
            holder.itemView.setOnClickListener {
                listener.onClick(episode)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}