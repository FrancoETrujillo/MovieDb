package com.ftrujillo.moviedbsample.domain.datamodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftrujillo.moviedbsample.data.data_source.dto.popular_movies.PopularMovieDto
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movie_table")
data class Movie(
    val backdrop_path: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val title: String
) : Parcelable

fun PopularMovieDto.toMovie(): Movie {
    return Movie(
        backdrop_path = this.backdrop_path,
        id = this.id,
        overview = this.overview,
        poster_path = this.poster_path,
        title = this.title,
        popularity = this.popularity
    )
}