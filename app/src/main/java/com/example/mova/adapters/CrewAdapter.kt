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
import com.example.mova.adapters.callbacks.OnCrewClickListener
import com.example.mova.data.movie.Crew
import com.example.mova.databinding.PopularPeopleItemBinding
import com.example.mova.utils.Constants

class CrewAdapter(
    private val listener: OnCrewClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<CrewAdapter.CrewVH>() {

    inner class CrewVH(val binding: PopularPeopleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Crew>() {
        override fun areItemsTheSame(oldItem: Crew, newItem: Crew) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Crew, newItem: Crew) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CrewVH(
            PopularPeopleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: CrewVH, position: Int) {
        val crew = differ.currentList[position]
        holder.binding.apply {
            crew.profile_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + crew.profile_path)
                    .transform(CenterCrop(), RoundedCorners(1000))
                    .into(imageViewPerson)
            } ?: Glide.with(context).load(R.drawable.series)
                .transform(CenterCrop(), RoundedCorners(1000))
                .into(imageViewPerson)
            tvName.isSelected = true
            tvName.text = crew.name
            holder.itemView.setOnClickListener {
                listener.onClick(crew)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}