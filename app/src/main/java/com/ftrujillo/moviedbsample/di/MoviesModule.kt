package com.ftrujillo.moviedbsample.di

import com.ftrujillo.moviedbsample.data.repository.MoviesRepositoryImpl
import com.ftrujillo.moviedbsample.presentation.movie_list.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesModule = module {
    single { MoviesRepositoryImpl(get()) }
    viewModel { MoviesViewModel(get()) }
}