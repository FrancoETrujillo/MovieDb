package com.ftrujillo.moviedbsample.presentation.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ftrujillo.moviedbsample.data.repository.MoviesRepository
import com.ftrujillo.moviedbsample.data.repository.MoviesRepositoryImpl
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesRepository: MoviesRepository): ViewModel() {


    val movieList = moviesRepository.getPopularMovies().asLiveData()
}