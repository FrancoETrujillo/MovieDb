package com.ftrujillo.moviedbsample.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ftrujillo.moviedbsample.data.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesRepository: MoviesRepository): ViewModel() {
    fun refreshContent() {
        viewModelScope.launch {
            moviesRepository.refresh()
        }
    }

    val movieList = moviesRepository.popularMovies.asLiveData()
}