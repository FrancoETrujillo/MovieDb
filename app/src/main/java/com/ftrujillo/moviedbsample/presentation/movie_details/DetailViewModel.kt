package com.ftrujillo.moviedbsample.presentation.movie_details

import androidx.lifecycle.*
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.data.repository.MoviesRepository
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails
import com.ftrujillo.moviedbsample.presentation.movie_list.MovieFragmentDirections
import kotlinx.coroutines.launch

class DetailViewModel(
    private val handle: SavedStateHandle,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _movieDetailLiveData = MutableLiveData<MovieDetails>()
    val movieDetailLiveData = _movieDetailLiveData as LiveData<MovieDetails>
    fun getMovie() {
        viewModelScope.launch {
            handle.get<Int>("movieId")?.let { id ->
                moviesRepository.getMovieDetails(id).collect {
                    when (it) {
                        is RequestDataWrapper.Error -> {
                            it.result?.let { movieDetails ->
                                _movieDetailLiveData.postValue(movieDetails)
                            }
                        }
                        is RequestDataWrapper.Loading -> Unit
                        is RequestDataWrapper.Success -> _movieDetailLiveData.postValue(it.result)
                    }
                }
            }
        }
    }
}