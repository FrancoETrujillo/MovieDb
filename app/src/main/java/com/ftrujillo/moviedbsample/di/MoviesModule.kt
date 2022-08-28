package com.ftrujillo.moviedbsample.di

import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.core.utils.AppDispatchers
import com.ftrujillo.moviedbsample.core.utils.DispatcherProvider
import com.ftrujillo.moviedbsample.core.utils.NetworkInfoProvider
import com.ftrujillo.moviedbsample.data.data_source.interceptors.ConnectivityInterceptor
import com.ftrujillo.moviedbsample.data.data_source.interceptors.MovieDbAuthInterceptor
import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApiSource
import com.ftrujillo.moviedbsample.data.data_source.remote.MoviesRemoteSource
import com.ftrujillo.moviedbsample.data.repository.MoviesRepository
import com.ftrujillo.moviedbsample.data.repository.MoviesRepositoryImpl
import com.ftrujillo.moviedbsample.data.storage.MovieDataBase
import com.ftrujillo.moviedbsample.presentation.movie_details.DetailViewModel
import com.ftrujillo.moviedbsample.presentation.movie_list.MoviesViewModel
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

val moviesModule = module {
    single<DispatcherProvider> { AppDispatchers() }
    viewModel { MoviesViewModel(get()) }
    viewModel { DetailViewModel(get(), get()) }
}

val dataModule = module {
    single { createMovieApi(BuildConfig.MOVIE_API_BASE_URL, get()) }
    single<MoviesRemoteSource> { MovieDbApiSource(get()) }
    single { createMovieDatabase(androidContext())}
    single { get<MovieDataBase>().moviesDao()}
}

val networkModule = module {
    single<MoviesRepository> { MoviesRepositoryImpl(get(), get(), get()) }
    single { NetworkInfoProvider(get()) }
    factory<Interceptor>(named("ConnInterceptor")) { ConnectivityInterceptor(get()) }
    factory<Interceptor>(named("AuthInterceptor")) { MovieDbAuthInterceptor() }
    single {
        createOkHttpClient(get(named("ConnInterceptor")), get(named("AuthInterceptor")))
    }
}

