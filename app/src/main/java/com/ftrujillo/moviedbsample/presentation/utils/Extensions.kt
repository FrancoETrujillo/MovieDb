package com.ftrujillo.moviedbsample.presentation.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ftrujillo.moviedbsample.R

fun ImageView.loadFromUrl(
    url: String,
    backupUrl: String? = null,
    placeHolder: Int = R.drawable.ic_baseline_image_24
) {
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(placeHolder)
        .error(
            Glide.with(this)
                .load(backupUrl)
                .placeholder(placeHolder)
        )
        .into(this)
}