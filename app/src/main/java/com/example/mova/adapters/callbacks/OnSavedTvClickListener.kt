package com.example.mova.adapters.callbacks

import com.example.mova.data.tv.Tv

interface OnSavedTvClickListener {
    fun onClick(tv: Tv)
    fun onSavedClick(tv: Tv)
}