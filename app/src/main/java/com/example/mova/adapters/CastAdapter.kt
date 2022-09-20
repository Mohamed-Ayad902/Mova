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
import com.example.mova.adapters.callbacks.OnCastClickListener
import com.example.mova.data.movie.Cast
import com.example.mova.databinding.PopularPeopleItemBinding
import com.example.mova.utils.Constants

class CastAdapter(
    private val listener: OnCastClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<CastAdapter.CastVH>() {

    inner class CastVH(val binding: PopularPeopleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CastVH(
            PopularPeopleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: CastVH, position: Int) {
        val cast = differ.currentList[position]
        holder.binding.apply {
            cast.profile_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + cast.profile_path)
                    .transform(CenterCrop(), RoundedCorners(1000))
                    .into(imageViewPerson)
            } ?: Glide.with(context).load(R.drawable.series)
                .transform(CenterCrop(), RoundedCorners(1000))
                .into(imageViewPerson)
            tvName.isSelected = true
            tvName.text = cast.name
            holder.itemView.setOnClickListener {
                listener.onClick(cast)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}