package com.ftrujillo.moviedbsample.data.remote.entity

import com.google.gson.annotations.SerializedName

data class PopularMoviesResponse(
    val page: Int,
    @SerializedName("results")
    val movies: List<RemotePopularMovie>,
    val total_pages: Int,
    val total_results: Int
)