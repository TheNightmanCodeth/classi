package me.thenightmancodeth.classi.injection.component

import dagger.Component
import me.thenightmancodeth.classi.injection.module.Bindings
import me.thenightmancodeth.classi.injection.module.NetworkModule
import javax.inject.Singleton

/**
 * Created by joe on 4/5/17.
 */

@Singleton
@Component(modules = arrayOf(Bindings::class, NetworkModule::class))
interface AppComponent {
    fun AppComponent(): ActivityComponent
}