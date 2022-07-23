package com.ftrujillo.moviedbsample.presentation.movie_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
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

        viewModel.movieList.observe(this.viewLifecycleOwner) { requestState ->
            when (requestState){
                is RequestDataWrapper.Success -> {
                    Timber.d("MovieList received ${requestState.result}")
                    binding.loadingViews.groupLoading.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.refreshLayout.isRefreshing = false
                    requestState.result?.let { adapter.updateList(it) }
                }
                is RequestDataWrapper.Loading -> {
                    binding.loadingViews.groupLoading.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is RequestDataWrapper.Error -> {
                    binding.loadingViews.groupLoading.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                }
            }

        }

        binding.refreshLayout.setOnRefreshListener {
            adapter.updateList(emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}