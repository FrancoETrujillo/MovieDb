package com.ftrujillo.moviedbsample.data.repository

import com.ftrujillo.moviedbsample.core.utils.DispatcherProvider
import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.core.utils.toLoadingResultWrapper
import com.ftrujillo.moviedbsample.data.data_source.remote.MoviesRemoteSource
import com.ftrujillo.moviedbsample.data.storage.MoviesDao
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

class MoviesRepositoryImpl(
    private val moviesRemoteSource: MoviesRemoteSource,
    private val dispatcherProvider: DispatcherProvider,
    private val moviesDao: MoviesDao
) : MoviesRepository {

    override fun getPopularMovies(forced: Boolean): Flow<RequestDataWrapper<List<Movie>>> = flow {
        emit(RequestDataWrapper.Loading())
        if (forced) {
            val moviesResponse = fetchAndSaveMovies()
            emit(moviesResponse)
        } else {
            val moviesCount = moviesDao.getSavedMoviesCount()
            if (moviesCount != 0) {
                val movies = moviesDao.getMovies()
                emit(RequestDataWrapper.Success(movies))
            } else {
                emit(fetchAndSaveMovies())
            }
        }
    }.flowOn(dispatcherProvider.io)

    private suspend fun fetchAndSaveMovies(): RequestDataWrapper<List<Movie>> {
        val moviesResponse =
            moviesRemoteSource.getPopularMoviesByPage(Locale.getDefault().language, 1)
        return when (moviesResponse) {
            is RemoteRequestWrapper.Success -> {
                if (moviesResponse.result.isNotEmpty()) {
                    moviesDao.upsertMovies(moviesResponse.result)
                    RequestDataWrapper.Success(moviesDao.getMovies())
                } else {
                    RequestDataWrapper.Error("Not movies found")
                }
            }
            is RemoteRequestWrapper.Error -> {
                val moviesCount = moviesDao.getSavedMoviesCount()
                return if (moviesCount == 0) {
                    moviesResponse.toLoadingResultWrapper()
                } else {
                    RequestDataWrapper.Error(moviesResponse.errorMessage, moviesDao.getMovies())
                }
            }
        }
    }


    override fun getMovieDetails(movieId: Int) = flow {
        emit(RequestDataWrapper.Loading())
        emit(fetchAndSaveMovieDetail(movieId))
    }.flowOn(dispatcherProvider.io)

    private suspend fun fetchAndSaveMovieDetail(movieId: Int): RequestDataWrapper<MovieDetails> {
        val movieDetailResponse = moviesRemoteSource.getMovieDetails(movieId)

        return when (movieDetailResponse) {
            is RemoteRequestWrapper.Success -> {
                moviesDao.upsertMoviesDetails(movieDetailResponse.result)
                RequestDataWrapper.Success(moviesDao.getMovieDetail(movieId)!!)
            }
            is RemoteRequestWrapper.Error -> {
                val movieDetail = moviesDao.getMovieDetail(movieId)
                return RequestDataWrapper.Error(movieDetailResponse.errorMessage, movieDetail)
            }
        }
    }

}