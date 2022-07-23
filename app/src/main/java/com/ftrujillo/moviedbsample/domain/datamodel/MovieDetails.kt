package com.ftrujillo.moviedbsample.domain.datamodel

import com.ftrujillo.moviedbsample.data.data_source.dto.movie_details.*

data class MovieDetails(
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val revenue: Int,
    val title: String,
    val voteAverage: Double,
)

fun MovieDetailsDto.toMovieDetails(): MovieDetails {
    return MovieDetails(
        this.genres,
        this.homepage,
        this.id,
        this.original_title,
        this.overview,
        this.popularity,
        this.poster_path,
        this.release_date,
        this.revenue,
        this.title,
        this.vote_average
    )
}
