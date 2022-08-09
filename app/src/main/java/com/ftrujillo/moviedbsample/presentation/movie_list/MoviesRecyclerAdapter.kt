package com.ftrujillo.moviedbsample.presentation.movie_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.databinding.MovieItemViewBinding
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.squareup.picasso.Picasso
import timber.log.Timber

class MoviesRecyclerAdapter :
    RecyclerView.Adapter<MoviesRecyclerAdapter.MovieItemViewHolder>() {

    var movieList: List<Movie>
        get() = asyncDiffer.currentList
        set(value) = asyncDiffer.submitList(value)

    var onMovieClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding =
            MovieItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val movie = movieList[position]
        holder.binding.apply {
            textMovieCard.text = movie.title
            val imageUrl = "${BuildConfig.MOVIE_API_POSTER_BASE_URL}${movie.poster_path}"
            Timber.d("Trying to load image from: $imageUrl")
            Picasso.get().load(imageUrl)
                .into(imageMovieCard)
            root.setOnClickListener {
                onMovieClickListener?.invoke(movie)
            }
        }
    }

    override fun getItemCount() = movieList.size

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncDiffer = AsyncListDiffer(this, diffCallback)

    inner class MovieItemViewHolder(val binding: MovieItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}