package com.ftrujillo.moviedbsample.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.ftrujillo.moviedbsample.R
import com.ftrujillo.moviedbsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavComponents()

    }

    private fun setupBottomNavComponents() {
        setSupportActionBar(binding.toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.movies -> {
                    // Respond to navigation item 1 click
                    true
                }
//                R.id.tv -> {
//                    // Respond to navigation item 2 click
//                    true
//                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.MovieFragment, R.id.TvFragment -> binding.bottomNavigation.visibility =
                    View.VISIBLE
                else -> binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}