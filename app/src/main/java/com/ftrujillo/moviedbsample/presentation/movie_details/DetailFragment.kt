package com.ftrujillo.moviedbsample.presentation.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.getViewModel

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(
            LayoutInflater.from(this.requireContext()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.getMovie(args.movieId).observe(viewLifecycleOwner) { movie ->
            when (movie) {
                is RequestDataWrapper.Success -> {
                    movie.result?.let { movieDetail ->
                        val imageUrl =
                            "${BuildConfig.MOVIE_API_POSTER_BASE_URL}${movieDetail.posterPath}"
                        binding.apply {
                            overviewTxt.text = movieDetail.overview
                            movieTitleTxt.text = movieDetail.title
                            releaseDateTxt.text = movieDetail.releaseDate
                            Picasso.get().load(imageUrl)
                                .into(movieImg)
                        }
                    }
                }

                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}