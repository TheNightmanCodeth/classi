package me.thenightmancodeth.classi.ui.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by joe on 4/5/17.
 */

abstract class BasePresenter<T: MvpView> {

    protected val disposables = CompositeDisposable()
    protected var view: T? = null

    fun bind(view: T) {
        this.view = view
    }

    fun unbind() {
        this.view = null
    }

    fun destroy() {
        disposables.clear()
        unbind()
    }
}
