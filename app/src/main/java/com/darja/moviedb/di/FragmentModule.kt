package com.darja.moviedb.di

import com.darja.moviedb.ui.fragment.movieslist.MoviesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun searchMoviesListFragment(): MoviesListFragment
}