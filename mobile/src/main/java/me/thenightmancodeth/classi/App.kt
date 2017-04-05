package me.thenightmancodeth.classi

import android.app.Application
import me.thenightmancodeth.classi.injection.component.AppComponent
import me.thenightmancodeth.classi.injection.component.DaggerAppComponent

/**
 * Created by joe on 4/5/17.
 */

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        setupComponent()
    }

    private fun setupComponent() {
        appComponent = DaggerAppComponent.builder().build()
    }
}
