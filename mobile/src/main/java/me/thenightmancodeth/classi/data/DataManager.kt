package me.thenightmancodeth.classi.data

import me.thenightmancodeth.classi.data.model.User
import io.reactivex.Observable
import me.thenightmancodeth.classi.data.model.Class

/**
 * Created by joe on 4/5/17.
 */

interface DataManager {

    fun getUserData(uid: Int): Observable<User>

    fun getUserClasses(count: Int): Observable<List<Class>>

}