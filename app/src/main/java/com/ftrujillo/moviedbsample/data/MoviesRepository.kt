package com.ftrujillo.moviedbsample.data

import com.ftrujillo.moviedbsample.data.remote.MovieDbApi
import com.ftrujillo.moviedbsample.data.remote.entity.PopularMoviesResponse
import com.ftrujillo.moviedbsample.data.remote.entity.RemotePopularMovie
import com.ftrujillo.moviedbsample.ui.datamodel.Movie
import com.ftrujillo.moviedbsample.ui.datamodel.toMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Locale

class MoviesRepository(private val movieDbApi: MovieDbApi) {

    private val _movies = MutableSharedFlow<Response<PopularMoviesResponse>>()

    suspend fun refresh() {
        withContext(Dispatchers.IO){
            val moviesResponse = movieDbApi.getPopularMoviesByPage(Locale.getDefault().language, 1)

            if (moviesResponse.isSuccessful) {
                _movies.emit(moviesResponse)
            }
        }
    }

    val popularMovies: Flow<List<Movie>> = _movies
        .filter { it.isSuccessful }
        .mapNotNull {  response -> response.body()?.movies?.map { it.toMovie() }  }



}