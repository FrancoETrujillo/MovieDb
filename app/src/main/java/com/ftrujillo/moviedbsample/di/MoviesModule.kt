package com.ftrujillo.moviedbsample.di

import com.ftrujillo.moviedbsample.data.MoviesRepository
import com.ftrujillo.moviedbsample.ui.movie.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesModule = module {
    single { MoviesRepository(get()) }
    viewModel { MoviesViewModel(get()) }
}