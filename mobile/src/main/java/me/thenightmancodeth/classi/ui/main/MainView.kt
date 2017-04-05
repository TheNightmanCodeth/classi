package me.thenightmancodeth.classi.ui.main

import me.thenightmancodeth.classi.data.model.Class
import me.thenightmancodeth.classi.ui.base.MvpView

/**
 * Created by joe on 4/5/17.
 */

interface MainView : MvpView {
    fun onFetchClassSuccess(classes: List<Class>)
    fun onFetchClassError(error: Throwable)
}
