package com.ftrujillo.moviedbsample.data.data_source.dto.movie_details

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val name: String
)