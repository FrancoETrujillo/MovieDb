package com.ftrujillo.moviedbsample.di

import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApi
import com.ftrujillo.moviedbsample.data.data_source.interceptors.ConnectivityInterceptor
import com.ftrujillo.moviedbsample.data.data_source.interceptors.MovieDbAuthInterceptor
import com.ftrujillo.moviedbsample.core.utils.NetworkInfoProvider
import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApiSource
import com.ftrujillo.moviedbsample.data.data_source.remote.MoviesRemoteSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { NetworkInfoProvider(get()) }
    factory<Interceptor>(named("ConnInterceptor")) { ConnectivityInterceptor(get()) }
    factory<Interceptor>(named("AuthInterceptor")) { MovieDbAuthInterceptor() }
    single<MoviesRemoteSource> { MovieDbApiSource(get()) }
    single<MovieDbApi> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                provideOkHttpClient(
                    get(named("ConnInterceptor")),
                    get(named("AuthInterceptor"))
                )
            )
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