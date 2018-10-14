@file:Suppress("ProtectedInFinal")

package com.darja.moviedb.ui.fragment.movieslist

import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.darja.moviedb.R

class MoviesListFragmentView {
//    @BindView(R.id.list) protected lateinit var list: RelativeLayout
    @BindView(R.id.empty_message) protected lateinit var emptyMessage: TextView
    @BindView(R.id.progress) protected lateinit var progress: View

    fun setProgressVisibility(visible: Boolean?) {
        progress.visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    fun showEmptyMessage(message: String) {
        emptyMessage.text = message
        emptyMessage.visibility = View.VISIBLE
    }

    fun hideEmptyMessage() {
        emptyMessage.visibility = View.GONE
    }
}