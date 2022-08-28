package com.ftrujillo.moviedbsample.domain.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.data.data_source.dto.movie_details.Genre
import com.ftrujillo.moviedbsample.data.data_source.dto.movie_details.MovieDetailsDto

@Entity(tableName = "movie_details")
data class MovieDetails(
    val genres: List<Genre>,
    val homepage: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
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
        "${BuildConfig.MOVIE_API_POSTER_BASE_URL}${this.poster_path}",
        this.release_date,
        this.revenue,
        this.title,
        this.vote_average,
    )
}
