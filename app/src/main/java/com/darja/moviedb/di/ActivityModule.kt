package com.darja.moviedb.di

import com.darja.moviedb.activity.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [(FragmentModule::class)])
    abstract fun mainActivity(): MainActivity
}