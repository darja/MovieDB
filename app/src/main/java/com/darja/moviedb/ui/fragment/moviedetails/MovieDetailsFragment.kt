package com.darja.moviedb.ui.fragment.moviedetails

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeViewModel()
        setupViewEvents()
    }

    private fun setupViewEvents() {
        view.setHomepageClickListener(View.OnClickListener {
            val homepage = viewModel.getMovie().value?.homepage
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(homepage))
            startActivity(intent)
        })
    }

    private fun observeViewModel() {
        viewModel.getMovie().observe(this, Observer {
            if (it != null) {
                view.bindMovieDetails(it)
            }
        })
        viewModel.movieLoadedLiveData.observe(this, Observer { loaded ->
            if (loaded == false) {
                view.showError(activity, getString(R.string.error_cannot_load_movie_details))
            }
        })
    }
}