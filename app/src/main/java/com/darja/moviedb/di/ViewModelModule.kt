package com.darja.moviedb.di
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.darja.moviedb.activity.main.MainViewModel
import com.darja.moviedb.ui.fragment.moviedetails.MovieDetailsViewModel
import com.darja.moviedb.ui.fragment.movieslist.MoviesListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindsViewModelFactory(vmFactory: VMFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(vm: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoviesListViewModel::class)
    abstract fun bindsMoviesListViewModel(vm: MoviesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindsMovieDetailsViewModel(vm: MovieDetailsViewModel): ViewModel
}