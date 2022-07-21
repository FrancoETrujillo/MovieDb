package com.ftrujillo.moviedbsample.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.databinding.MovieItemViewBinding
import com.ftrujillo.moviedbsample.ui.datamodel.Movie
import com.squareup.picasso.Picasso
import timber.log.Timber

class MoviesRecyclerAdapter() :
    RecyclerView.Adapter<MoviesRecyclerAdapter.MovieItemViewHolder>() {
    private val movieList:MutableList<Movie> = mutableListOf()

    fun updateList(_movieList: List<Movie>) {
        movieList.clear()
        movieList.addAll(_movieList)
        notifyDataSetChanged()
    }

    class MovieItemViewHolder(private val binding: MovieItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.textMovieCard.text = movie.title
            val imageUrl = "${BuildConfig.MOVIE_API_POSTER_BASE_URL}${movie.poster_path}"
            Timber.d("Trying to load image from: $imageUrl")
            Picasso.get().load(imageUrl)
                .into(binding.imageMovieCard);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding =
            MovieItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size

}