package me.thenightmancodeth.classi.data.remote

import me.thenightmancodeth.classi.data.model.Response
import me.thenightmancodeth.classi.data.model.User
import me.thenightmancodeth.classi.data.model.Class
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by joe on 4/5/17.
 */

interface ClassiService {

    @GET("/{uid}")
    fun fetchUserData(@Path("uid")uid: Int): Observable<User>

    @GET("/{uid}/classes")
    fun fetchUserClasses(@Path("uid")uid: Int): Observable<Response<Class>>

}
