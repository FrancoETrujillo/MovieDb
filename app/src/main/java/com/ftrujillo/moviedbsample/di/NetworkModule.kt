package com.ftrujillo.moviedbsample.di

import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.data.remote.MovieDbApi
import com.ftrujillo.moviedbsample.data.remote.interceptors.ConnectivityInterceptor
import com.ftrujillo.moviedbsample.data.remote.interceptors.MovieDbAuthInterceptor
import com.ftrujillo.moviedbsample.utils.NetworkInfoProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { NetworkInfoProvider(get()) }
    factory { ConnectivityInterceptor(get()) }
    factory { MovieDbAuthInterceptor() }
    single<MovieDbApi> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(get(), get()))
            .build()
            .create(MovieDbApi::class.java)
    }
}


fun provideOkHttpClient(vararg interceptors: Interceptor): OkHttpClient {
    val client = OkHttpClient().newBuilder().also { builder ->
        interceptors.forEach {
            builder.addInterceptor(it)
        }
    }
    return client.build()
}