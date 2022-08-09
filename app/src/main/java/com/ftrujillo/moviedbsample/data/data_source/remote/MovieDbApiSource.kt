package com.ftrujillo.moviedbsample.data.data_source.remote

import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails
import com.ftrujillo.moviedbsample.domain.datamodel.toMovie
import com.ftrujillo.moviedbsample.domain.datamodel.toMovieDetails
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class MovieDbApiSource(private val movieDbApi: MovieDbApi) : MoviesRemoteSource {
    override suspend fun getPopularMoviesByPage(
        language: String, page: Int
    ): RemoteRequestWrapper<List<Movie>> {
        return try {
            val moviesResponse =
                movieDbApi.getPopularMoviesByPage(Locale.getDefault().language, 1)
            if (moviesResponse.isSuccessful) {
                RemoteRequestWrapper.Success(moviesResponse.body()?.movies?.map { it.toMovie() }
                    ?: listOf())
            } else {
                RemoteRequestWrapper.Error(moviesResponse.errorBody().toString())
            }
        } catch (io: IOException) {
            RemoteRequestWrapper.Error("Check your Internet Connection")

        } catch (e: HttpException) {
            RemoteRequestWrapper.Error("Problem while reaching the server")
        }
    }

    override suspend fun getMovieDetails(movieId: Int): RemoteRequestWrapper<MovieDetails> {
        return try {
            val movieDetailResponse =
                movieDbApi.getMovieDetails(movieId)
            if (movieDetailResponse.isSuccessful) {
                movieDetailResponse.body()
                    ?.let { RemoteRequestWrapper.Success(it.toMovieDetails()) }
                    ?: RemoteRequestWrapper.Error("No movie found")
            } else {
                RemoteRequestWrapper.Error(movieDetailResponse.errorBody().toString())
            }
        } catch (io: IOException) {
            RemoteRequestWrapper.Error("Check your Internet Connection")

        } catch (e: HttpException) {
            RemoteRequestWrapper.Error("Problem while reaching the server")
        }
    }
}