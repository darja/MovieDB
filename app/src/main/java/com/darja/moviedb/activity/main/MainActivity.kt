package com.darja.moviedb.activity.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import butterknife.ButterKnife
import com.darja.moviedb.R
import com.darja.moviedb.activity.BaseActivity
import com.darja.moviedb.ui.MovieSelected
import com.darja.moviedb.ui.fragment.moviedetails.MovieDetailsFragment
import com.darja.moviedb.ui.fragment.movieslist.MoviesListFragment
import com.darja.moviedb.util.DPLog
import dagger.android.AndroidInjection
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class MainActivity: BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel
    private var view = MainActivityView()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(view, this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        observeViewModel()
        if (savedInstanceState == null) {
            viewModel.clearOutdated()
            viewModel.updateGenres()
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun observeViewModel() {
        viewModel.initialized.observe(this, Observer {
            if (supportFragmentManager.fragments.isEmpty()) {
                view.hideProgress()
                showSearchResult()
            }
        })
    }

    private fun showSearchResult() {
        DPLog.checkpoint()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MoviesListFragment())
            .commit()
    }

    @Suppress("ProtectedInFinal")
    @Subscribe
    protected fun onMovieSelected(event: MovieSelected) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MovieDetailsFragment.newInstance(event.movie.movieId))
            .addToBackStack(null)
            .commit()
    }
}
