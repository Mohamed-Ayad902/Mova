package com.example.mova.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mova.R
import com.example.mova.data.ProductionCompany
import com.example.mova.databinding.ProductionCompanyItemBinding
import com.example.mova.utils.Constants

class ProductionCompanyAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<ProductionCompanyAdapter.ProductionCompanyVH>() {

    inner class ProductionCompanyVH(val binding: ProductionCompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<ProductionCompany>() {
        override fun areItemsTheSame(oldItem: ProductionCompany, newItem: ProductionCompany) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductionCompany, newItem: ProductionCompany) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductionCompanyVH(
            ProductionCompanyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ProductionCompanyVH, position: Int) {
        val company = differ.currentList[position]
        holder.binding.apply {
            company.logo_path?.let {
                Glide.with(context).load(Constants.IMAGE_URL + company.logo_path)
                    .into(imageView)
            } ?: Glide.with(context).load(R.drawable.series)
                .into(imageView)
            tvCompanyName.text = company.name
        }
    }

    override fun getItemCount() = differ.currentList.size


}