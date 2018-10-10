package com.darja.moviedb

import android.app.Activity
import android.app.Application
import com.darja.moviedb.di.AppModule
import com.darja.moviedb.di.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MovieDbApp @Inject constructor() : Application(), HasActivityInjector {

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        initInjector()
        Fresco.initialize(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = dispatchingAndroidInjector

    private fun initInjector() {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }
}