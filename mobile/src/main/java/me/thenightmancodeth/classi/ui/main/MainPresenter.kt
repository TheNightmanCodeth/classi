package me.thenightmancodeth.classi.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import me.thenightmancodeth.classi.data.DataManager
import me.thenightmancodeth.classi.data.model.Class
import me.thenightmancodeth.classi.injection.scope.PerActivity
import me.thenightmancodeth.classi.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by joe on 4/5/17.
 */
@PerActivity
class MainPresenter @Inject constructor(val dataManager: DataManager) : BasePresenter<MainView>() {

    fun fetchClasses() {
        disposables.add(
                dataManager.getUserClasses(0)
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe(
                                { onFetchClassSuccess(it) },
                                { onFetchClassError(it) })
        )
    }

    fun onFetchClassSuccess(classes: List<Class>) {
        view?.onFetchClassSuccess(classes)
    }

    fun onFetchClassError(error: Throwable) {
        view?.onFetchClassError(error)
    }
}