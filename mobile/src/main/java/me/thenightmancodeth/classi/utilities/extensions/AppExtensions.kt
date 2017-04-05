package me.thenightmancodeth.classi.utilities.extensions

import android.content.Context
import me.thenightmancodeth.classi.App
import me.thenightmancodeth.classi.injection.component.AppComponent

/**
 * Created by joe on 4/5/17.
 */

fun Context.getAppComponent(): AppComponent = (applicationContext as App).appComponent
