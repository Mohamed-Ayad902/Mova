package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.adapters.callbacks.OnQuoteClickListener
import com.example.mova.data.quote.Quote
import com.example.mova.databinding.QuoteItemBinding

class QuotesAdapter(
    private val listener: OnQuoteClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<QuotesAdapter.CastVH>() {

    inner class CastVH(val binding: QuoteItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote) =
            oldItem.slug == newItem.slug

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CastVH(
            QuoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: CastVH, position: Int) {
        val quote = differ.currentList[position]
        holder.binding.apply {
            Glide.with(context).load(quote.image).into(imageView)
            tvName.text = quote.name
            holder.itemView.setOnClickListener {
                listener.onClick(quote)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


}