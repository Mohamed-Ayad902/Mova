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
import com.example.mova.adapters.callbacks.OnPopularPeopleClickListener
import com.example.mova.data.people.Result
import com.example.mova.databinding.PopularPeopleItemBinding
import com.example.mova.utils.Constants

class PopularPeopleAdapter(
    private val listener: OnPopularPeopleClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<PopularPeopleAdapter.PopularPeopleVH>() {

    inner class PopularPeopleVH(val binding: PopularPeopleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PopularPeopleVH(
            PopularPeopleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: PopularPeopleVH, position: Int) {
        val people = differ.currentList[position]
        holder.binding.apply {
            people.profile_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + people.profile_path)
                    .transform(CenterCrop(), RoundedCorners(1000))
                    .into(imageViewPerson)
            } ?: Glide.with(context).load(R.drawable.series)
                .transform(CenterCrop(), RoundedCorners(1000))
                .into(imageViewPerson)
            tvName.isSelected = true
            tvName.text = people.name
            holder.itemView.setOnClickListener {
                listener.onClick(people)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}