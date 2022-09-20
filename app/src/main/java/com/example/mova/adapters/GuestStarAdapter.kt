package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnGuestClickListener
import com.example.mova.data.tv_season.GuestStar
import com.example.mova.databinding.CastItemBinding
import com.example.mova.utils.Constants

class GuestStarAdapter(
    private val listener: OnGuestClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<GuestStarAdapter.GuestStarVH>() {

    inner class GuestStarVH(val binding: CastItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<GuestStar>() {
        override fun areItemsTheSame(oldItem: GuestStar, newItem: GuestStar) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GuestStar, newItem: GuestStar) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GuestStarVH(
            CastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: GuestStarVH, position: Int) {
        val guest = differ.currentList[position]
        holder.binding.apply {
            guest.profile_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + guest.profile_path)
                    .transform(CenterCrop(), RoundedCorners(1000))
                    .into(imageViewPerson)
            } ?: Glide.with(context).load(R.drawable.series)
                .transform(CenterCrop(), RoundedCorners(1000))
                .into(imageViewPerson)
            tvName.isSelected = true
            tvName.text = guest.character
            tvOriginalName.text = guest.original_name
            holder.itemView.setOnClickListener {
                listener.onClick(guest)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}