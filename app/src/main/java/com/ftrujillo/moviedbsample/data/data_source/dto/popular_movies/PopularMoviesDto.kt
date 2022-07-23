package com.ftrujillo.moviedbsample.data.data_source.dto.popular_movies

import com.google.gson.annotations.SerializedName

data class PopularMoviesDto(
    val page: Int,
    @SerializedName("results")
    val movies: List<PopularMovieDto>,
    val total_pages: Int,
    val total_results: Int
)