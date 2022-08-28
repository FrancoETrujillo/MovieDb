package com.ftrujillo.moviedbsample.di

import android.content.Context
import androidx.room.Room
import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApi
import com.ftrujillo.moviedbsample.data.storage.MovieDataBase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun createMovieApi(baseUrl: String, okHttpClient: OkHttpClient): MovieDbApi {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
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

fun createMovieDatabase(context: Context, dbName: String = "DbName"): MovieDataBase {
    return Room.databaseBuilder(
        context,
        MovieDataBase::class.java,
        dbName
    ).fallbackToDestructiveMigration().build()
}