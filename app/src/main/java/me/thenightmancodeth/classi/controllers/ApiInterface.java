package me.thenightmancodeth.classi.controllers;

import me.thenightmancodeth.classi.models.data.User;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by thenightman on 1/8/17.
 **/

public interface ApiInterface {
    @GET("user/{id}")
    Observable<User> getUser(String id);
}
