package com.ftrujillo.moviedbsample.data.repository

import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApi
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails
import com.ftrujillo.moviedbsample.domain.datamodel.toMovie
import com.ftrujillo.moviedbsample.domain.datamodel.toMovieDetails
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApiSource
import com.ftrujillo.moviedbsample.data.data_source.remote.MoviesRemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class MoviesRepositoryImpl(private val moviesRemoteSource: MoviesRemoteSource) {

    fun getPopularMovies() = flow {
        emit(RequestDataWrapper.Loading())
        val movies = when (val moviesResponse =
            moviesRemoteSource.getPopularMoviesByPage(Locale.getDefault().language, 1)) {
            is RemoteRequestWrapper.Success -> RequestDataWrapper.Success(moviesResponse.result)
            is RemoteRequestWrapper.Error -> RequestDataWrapper.Error("${moviesResponse.errorMessage}")
        }
        emit(movies)

    }.flowOn(Dispatchers.IO)

    fun getMovieDetails(movieId: Int) = flow {
        emit(RequestDataWrapper.Loading())
        val movies = when (val moviesResponse =
            moviesRemoteSource.getMovieDetails(movieId)) {
            is RemoteRequestWrapper.Success -> RequestDataWrapper.Success(moviesResponse.result)
            is RemoteRequestWrapper.Error -> RequestDataWrapper.Error("${moviesResponse.errorMessage}")
        }
        emit(movies)
    }.flowOn(Dispatchers.IO)


}