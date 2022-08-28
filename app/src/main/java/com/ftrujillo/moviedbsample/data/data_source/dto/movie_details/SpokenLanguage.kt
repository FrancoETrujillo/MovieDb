package com.ftrujillo.moviedbsample.data.data_source.dto.movie_details

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)