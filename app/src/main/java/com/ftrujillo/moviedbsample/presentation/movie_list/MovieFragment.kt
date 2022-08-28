package com.ftrujillo.moviedbsample.presentation.movie_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.databinding.FragmentMovieBinding
import kotlinx.coroutines.launch
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
        viewModel.getMovies(false)
    }

    private fun initViews(){
        adapter = MoviesRecyclerAdapter()
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getMovies(true)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this.context, 3)
        binding.recyclerView.adapter = adapter
        adapter.onMovieClickListener = { movie ->
            Navigation.findNavController(binding.root).navigate(
                MovieFragmentDirections.actionMovieFragmentToDetailFragment(movie.id)
            )
        }
    }

    private fun setupObservers() {
        viewModel.movieList.observe(this.viewLifecycleOwner) { viewState ->
            showLoading(viewState.loading)
            adapter.movieList = viewState.movieList
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.promptFlow.collect{
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.refreshLayout.isRefreshing = show
        binding.loadingViews.groupLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}