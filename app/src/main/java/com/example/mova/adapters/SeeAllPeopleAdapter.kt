package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnPopularPeopleClickListener
import com.example.mova.data.people.Result
import com.example.mova.databinding.PeopleItemBinding
import com.example.mova.utils.Constants

class SeeAllPeopleAdapter(
    private val listener: OnPopularPeopleClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<SeeAllPeopleAdapter.SearchedPeopleVH>() {

    inner class SearchedPeopleVH(val binding: PeopleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchedPeopleVH(
            PeopleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: SearchedPeopleVH, position: Int) {
        val people = differ.currentList[position]
        holder.binding.apply {
            people.profile_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + people.profile_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
            tvName.text = people.name
            tvName.isSelected = true
            holder.itemView.setOnClickListener {
                listener.onClick(people)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}