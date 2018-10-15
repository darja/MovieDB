package com.darja.moviedb.ui.fragment.moviedetails

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.darja.moviedb.R
import com.darja.moviedb.ui.fragment.BaseFragment

class MovieDetailsFragment: BaseFragment<MovieDetailsViewModel, MovieDetailsFragmentView>() {
    companion object {
        private const val ARG_MOVIE_ID = "movie_id"

        fun newInstance(movieId: Long) : MovieDetailsFragment {
            val args = Bundle()
            args.putLong(ARG_MOVIE_ID, movieId)

            val fragment = MovieDetailsFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun createFragmentView() = MovieDetailsFragmentView()

    override fun getLayoutResId() = R.layout.fragment_movie_details

    override fun getViewModel() = MovieDetailsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.movieId = arguments?.getLong(ARG_MOVIE_ID) ?: 0
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getMovie().observe(this, Observer {
            if (it != null) {
                view.showMovieDetails(resources, it)
            } else {
                // todo show error message
            }
        })
    }
}