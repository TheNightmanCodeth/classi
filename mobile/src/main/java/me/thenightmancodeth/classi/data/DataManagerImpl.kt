package me.thenightmancodeth.classi.data

import io.reactivex.Observable
import me.thenightmancodeth.classi.data.model.Class
import me.thenightmancodeth.classi.data.model.User
import me.thenightmancodeth.classi.data.remote.ClassiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by joe on 4/5/17.
 */
@Singleton
internal class DataManagerImpl @Inject constructor(val classiService: ClassiService) : DataManager {
    override fun getUserClasses(count: Int): Observable<List<Class>> {
        return classiService
                .fetchUserClasses(count)
                .flatMap { Observable.just(it.value) }
    }

    override fun getUserData(uid: Int): Observable<User> {
        return classiService
                .fetchUserData(uid)
    }
}