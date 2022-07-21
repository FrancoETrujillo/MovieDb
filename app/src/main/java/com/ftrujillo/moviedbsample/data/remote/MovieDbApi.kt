package com.ftrujillo.moviedbsample.data.remote

import com.ftrujillo.moviedbsample.data.remote.entity.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbApi {

    @GET("movie/popular")
    suspend fun getPopularMoviesByPage(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
    ): Response<PopularMoviesResponse>
}