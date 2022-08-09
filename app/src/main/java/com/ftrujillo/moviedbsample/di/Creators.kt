package com.ftrujillo.moviedbsample.di

import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createMovieApi(baseUrl: String, okHttpClient: OkHttpClient): MovieDbApi {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(MovieDbApi::class.java)
}

fun createOkHttpClient(vararg interceptors: Interceptor): OkHttpClient {
    return OkHttpClient().newBuilder()
        .cache(null)
        .also { builder ->
            interceptors.forEach {
                builder.addInterceptor(it)
            }
        }.build()
}