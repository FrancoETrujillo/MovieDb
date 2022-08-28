package com.ftrujillo.moviedbsample.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ftrujillo.moviedbsample.data.data_source.dto.movie_details.Genre
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails

@Database(entities = [Movie::class, MovieDetails::class], version = 2, exportSchema = false)
@TypeConverters(DbTypeConverters::class)
abstract class MovieDataBase:RoomDatabase() {
    abstract fun moviesDao():MoviesDao
}