package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnTvShowClickListener
import com.example.mova.data.tv.Tv
import com.example.mova.databinding.TopMoviesSliderItemBinding
import com.example.mova.utils.Constants.Companion.IMAGE_URL

class TopTvShowsSlideAdapter(
    private val listener: OnTvShowClickListener,
    private val context: Context, private val viewPager: ViewPager2
) :
    RecyclerView.Adapter<TopTvShowsSlideAdapter.TopTvShowVH>() {

    inner class TopTvShowVH(val binding: TopMoviesSliderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Tv>() {
        override fun areItemsTheSame(oldItem: Tv, newItem: Tv) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Tv, newItem: Tv) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TopTvShowVH(
            TopMoviesSliderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: TopTvShowVH, position: Int) {
        val tv = differ.currentList[position]
        if (position == differ.currentList.size - 1) {
//            viewPager.post(runnable)
        }
        holder.binding.apply {
            tv.poster_path?.let {
                Glide.with(context).load(IMAGE_URL + tv.poster_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
//            textView.text = movie.title
            holder.itemView.setOnClickListener {
                listener.onClick(tv)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

//    private val runnable = Runnable {
//        differ.currentList.addAll(differ.currentList)
//        list.addAll(list)
//        notifyDataSetChanged()
//    }


}