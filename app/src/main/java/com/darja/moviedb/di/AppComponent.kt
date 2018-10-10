package com.darja.moviedb.di

import com.darja.moviedb.MovieDbApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Suppress("ReplaceArrayOfWithLiteral")
@Singleton
@Component(modules = arrayOf(
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    NetworkModule::class
))
interface AppComponent {
    fun inject(app: MovieDbApp)
}
