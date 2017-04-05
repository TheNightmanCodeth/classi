package me.thenightmancodeth.classi.ui.main.list

import android.content.Context
import me.thenightmancodeth.classi.data.model.Class
import me.thenightmancodeth.classi.injection.scope.PerActivity
import me.thenightmancodeth.classi.ui.base.list.BaseListAdapter
import me.thenightmancodeth.classi.ui.base.list.BaseViewHolder
import javax.inject.Inject

/**
 * Created by joe on 4/5/17.
 */
@PerActivity
class MainAdapter @Inject constructor() : BaseListAdapter<Class>() {

    override fun getListItemView(context: Context): BaseViewHolder<Class> {
        return ClassViewHolder(context)
    }
}