package com.ftrujillo.moviedbsample.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ftrujillo.moviedbsample.databinding.FragmentMovieBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieFragment : Fragment() {

    private lateinit var adapter: MoviesRecyclerAdapter
    private var _binding: FragmentMovieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = getViewModel<MoviesViewModel>()

        adapter = MoviesRecyclerAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this.context, 3)
        binding.recyclerView.adapter = adapter

        viewModel.movieList.observe(this.viewLifecycleOwner) {
            it?.let { movieList ->
                Timber.d("MovieList received $movieList")
                binding.loadingViews.groupLoading.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.refreshLayout.isRefreshing = false
                adapter.updateList(it)
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            adapter.updateList(emptyList())
            viewModel.refreshContent()
        }
        viewModel.refreshContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}