package com.ftrujillo.moviedbsample.data.data_source.dto.movie_details

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)