package com.darja.moviedb.ui.fragment

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel, VIEW: Any>: Fragment() {

    // view model factory instance
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // view model instance
    lateinit var viewModel: VM

    // view model instance
    lateinit var view: VIEW

    protected abstract fun getViewModel(): Class<VM>
    protected abstract fun createFragmentView(): VIEW
    protected abstract fun getLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(getLayoutResId(), container, false)

        view = createFragmentView()
        ButterKnife.bind(view, root)

        return root
    }
}