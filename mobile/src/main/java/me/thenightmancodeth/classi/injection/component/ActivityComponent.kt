package me.thenightmancodeth.classi.injection.component

import dagger.Subcomponent
import me.thenightmancodeth.classi.injection.scope.PerActivity
import me.thenightmancodeth.classi.ui.main.MainActivity

/**
 * Created by joe on 4/5/17.
 */

@PerActivity
@Subcomponent
interface ActivityComponent {

    fun inject(activity: MainActivity)

}