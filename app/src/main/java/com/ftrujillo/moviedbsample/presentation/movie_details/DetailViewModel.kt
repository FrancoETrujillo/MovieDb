package com.ftrujillo.moviedbsample.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ftrujillo.moviedbsample.data.repository.MoviesRepository

class DetailViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    fun getMovie(id: Int) = moviesRepository.getMovieDetails(id).asLiveData()
}