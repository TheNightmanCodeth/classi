package me.thenightmancodeth.classi.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import me.thenightmancodeth.classi.injection.component.ActivityComponent
import me.thenightmancodeth.classi.utilities.extensions.getAppComponent

/**
 * Created by joe on 4/5/17.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected var activityComponent: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())
        activityComponent = getAppComponent().AppComponent()
    }

    override fun onDestroy() {
        activityComponent = null
        super.onDestroy()
    }

    protected abstract fun getLayoutResID(): Int
}
