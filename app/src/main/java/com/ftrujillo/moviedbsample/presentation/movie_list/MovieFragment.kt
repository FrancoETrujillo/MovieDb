package com.ftrujillo.moviedbsample.presentation.movie_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.databinding.FragmentMovieBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MoviesViewModel
    private lateinit var adapter: MoviesRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()

        initViews()
        setupObservers()

    }

    private fun initViews(){
        adapter = MoviesRecyclerAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this.context, 3)
        binding.recyclerView.adapter = adapter
        adapter.onMovieClickListener = { movie ->
            Navigation.findNavController(binding.root).navigate(
                MovieFragmentDirections.actionMovieFragmentToDetailFragment(movie.id)
            )
        }
    }

    private fun setupObservers() {
        viewModel.movieList.observe(this.viewLifecycleOwner) { requestState ->
            when (requestState) {
                is RequestDataWrapper.Success -> {
                    showLoading(false)
                    binding.recyclerView.visibility = View.VISIBLE
                    requestState.result?.let { adapter.movieList = it }
                }
                is RequestDataWrapper.Loading -> {
                    showLoading(true)
                }
                is RequestDataWrapper.Error -> {
                    showLoading(false)
                    binding.loadingViews.groupLoading.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loadingViews.groupLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}