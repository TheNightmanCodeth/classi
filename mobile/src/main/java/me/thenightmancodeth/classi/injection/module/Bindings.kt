package me.thenightmancodeth.classi.injection.module

import dagger.Binds
import dagger.Module
import me.thenightmancodeth.classi.data.DataManager
import me.thenightmancodeth.classi.data.DataManagerImpl

/**
 * Created by joe on 4/5/17.
 */

@Module
abstract class Bindings {

    @Binds
    internal abstract fun bindDataManager(manager: DataManagerImpl): DataManager

}
