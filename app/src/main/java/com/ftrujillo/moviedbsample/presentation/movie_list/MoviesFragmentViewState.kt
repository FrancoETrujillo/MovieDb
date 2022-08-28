package com.ftrujillo.moviedbsample.presentation.movie_list

import com.ftrujillo.moviedbsample.domain.datamodel.Movie

data class MoviesFragmentViewState(
    val movieList: List<Movie> = emptyList(),
    val loading:Boolean = false
)
