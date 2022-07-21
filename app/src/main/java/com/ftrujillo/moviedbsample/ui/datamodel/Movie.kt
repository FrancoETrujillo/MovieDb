package com.ftrujillo.moviedbsample.ui.datamodel

import com.ftrujillo.moviedbsample.data.remote.entity.RemotePopularMovie

data class Movie(
    val backdrop_path: String,
    val id: Int,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val title: String
)

fun RemotePopularMovie.toMovie(): Movie {
    return Movie(
        backdrop_path = this.backdrop_path,
        id = this.id,
        overview = this.overview,
        poster_path = this.poster_path,
        title = this.title,
        popularity = this.popularity
    )
}