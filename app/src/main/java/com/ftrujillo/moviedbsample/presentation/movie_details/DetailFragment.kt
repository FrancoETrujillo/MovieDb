package com.ftrujillo.moviedbsample.presentation.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ftrujillo.moviedbsample.databinding.FragmentDetailBinding
import com.ftrujillo.moviedbsample.presentation.loadFromUrl
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by stateViewModel(state = {Bundle(arguments)})
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

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
        setupObservers()
        viewModel.getMovie()
    }

    private fun setupObservers() {
        viewModel.movieDetailLiveData.observe(viewLifecycleOwner) { movie ->
            movie.let { movieDetail ->
                binding.apply {
                    overviewTxt.text = movieDetail.overview
                    movieTitleTxt.text = movieDetail.title
                    releaseDateTxt.text = movieDetail.releaseDate
                    genresTxt.text = movieDetail.genres.joinToString(" , ") { it.name }
                    movieDetail.posterPath?.let { movieImg.loadFromUrl(it) }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}