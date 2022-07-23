package com.ftrujillo.moviedbsample.data.data_source.remote

import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails

interface MoviesRemoteSource {
    suspend fun getPopularMoviesByPage(
        language: String,
        page: Int
    ): RemoteRequestWrapper<List<Movie>>


    suspend fun getMovieDetails(movieId:Int): RemoteRequestWrapper<MovieDetails>
}