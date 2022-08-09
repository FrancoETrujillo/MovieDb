package com.ftrujillo.moviedbsample.data.repository

import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(): Flow<RequestDataWrapper<List<Movie>?>>
    fun getMovieDetails(movieId: Int): Flow<RequestDataWrapper<MovieDetails?>>
}