package com.ftrujillo.moviedbsample.data.data_source.remote

import com.ftrujillo.moviedbsample.data.data_source.dto.movie_details.MovieDetailsDto
import com.ftrujillo.moviedbsample.data.data_source.dto.popular_movies.PopularMoviesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET("movie/popular")
    suspend fun getPopularMoviesByPage(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
    ): PopularMoviesDto


    @GET("movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId:Int):MovieDetailsDto

}