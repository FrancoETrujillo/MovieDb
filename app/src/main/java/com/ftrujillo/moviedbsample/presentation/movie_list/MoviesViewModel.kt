package com.ftrujillo.moviedbsample.presentation.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.data.repository.MoviesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val _movieLiveData = MutableLiveData<MoviesFragmentViewState>()
    private val promptChannel = Channel<String>()
    val promptFlow = promptChannel.receiveAsFlow()

    fun getMovies(forced: Boolean) {
        viewModelScope.launch {
            moviesRepository.getPopularMovies(forced).collect {
                when (it) {
                    is RequestDataWrapper.Error -> {
                        if (!it.result.isNullOrEmpty()) {
                            promptChannel.send(it.errorMessage)
                            _movieLiveData.postValue(
                                MoviesFragmentViewState(
                                    it.result
                                )
                            )
                        }
                    }
                    is RequestDataWrapper.Loading -> _movieLiveData.postValue(
                        MoviesFragmentViewState(loading = true)
                    )
                    is RequestDataWrapper.Success -> _movieLiveData.postValue(
                        MoviesFragmentViewState(it.result)
                    )
                }
            }
        }
    }

    val movieList: LiveData<MoviesFragmentViewState> = _movieLiveData
}