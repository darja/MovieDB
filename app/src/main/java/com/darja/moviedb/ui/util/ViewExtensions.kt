package com.darja.moviedb.ui.util

import android.text.TextUtils
import android.view.View
import android.widget.TextView

fun TextView.setTextOrHide(text: String?) {
    if (TextUtils.isEmpty(text)) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
        this.text = text
    }
}