package com.ftrujillo.moviedbsample.data.data_source.remote

import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails
import com.ftrujillo.moviedbsample.domain.datamodel.toMovie
import com.ftrujillo.moviedbsample.domain.datamodel.toMovieDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class MovieDbApiSource(private val movieDbApi: MovieDbApi) : MoviesRemoteSource {
    override suspend fun getPopularMoviesByPage(
        language: String, page: Int
    ): RemoteRequestWrapper<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val moviesResponse =
                    movieDbApi.getPopularMoviesByPage(Locale.getDefault().language, 1)
                RemoteRequestWrapper.Success(moviesResponse.movies.map { it.toMovie() })
            } catch (io: IOException) {
                RemoteRequestWrapper.Error("Check your Internet Connection")

            } catch (e: HttpException) {
                RemoteRequestWrapper.Error("Problem while reaching the server")
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): RemoteRequestWrapper<MovieDetails> {
        return withContext(Dispatchers.IO) {
            try {
                val movieDetailResponse =
                    movieDbApi.getMovieDetails(movieId)
                RemoteRequestWrapper.Success(movieDetailResponse.toMovieDetails())
            } catch (io: IOException) {
                RemoteRequestWrapper.Error("Check your Internet Connection")

            } catch (e: HttpException) {
                RemoteRequestWrapper.Error("Problem while reaching the server")
            }
        }
    }
}