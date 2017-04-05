package me.thenightmancodeth.classi.ui.main.list

import android.content.Context
import me.thenightmancodeth.classi.R
import me.thenightmancodeth.classi.data.model.Class
import me.thenightmancodeth.classi.ui.base.list.BaseViewHolder

/**
 * Created by joe on 4/5/17.
 */

class ClassViewHolder(ctx: Context) : BaseViewHolder<Class>(ctx) {

    override fun layoutResId(): Int = R.layout.view_class

    override fun bind(item: Class) {

    }

}
