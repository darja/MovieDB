package com.darja.moviedb.activity.main

import android.view.View
import butterknife.BindView
import com.darja.moviedb.R

@Suppress("ProtectedInFinal")
class MainActivityView {
    @BindView(R.id.progress) protected lateinit var progress: View

    fun hideProgress() {
        progress.visibility = View.GONE
    }
}