package com.ftrujillo.moviedbsample.data.data_source.dto.popular_movies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMoviesDto(
    val page: Int,
    @field:Json(name = "results")
    val movies: List<PopularMovieDto>,
    val total_pages: Int,
    val total_results: Int
)