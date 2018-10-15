package com.darja.moviedb.di

import com.darja.moviedb.ui.fragment.moviedetails.MovieDetailsFragment
import com.darja.moviedb.ui.fragment.movieslist.MoviesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun moviesListFragment(): MoviesListFragment

    @ContributesAndroidInjector
    abstract fun movieDetailsFragment(): MovieDetailsFragment
}