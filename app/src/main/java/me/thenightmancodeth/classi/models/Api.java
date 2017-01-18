package me.thenightmancodeth.classi.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.thenightmancodeth.classi.controllers.ApiInterface;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.User;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thenightman on 1/8/17.
 **/

public class Api {
    private final ApiInterface apiInterface;
    private Realm realm;

    public Api(Realm r) {
        this.realm = r;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://thenightmancodeth.me/classi/api/")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public void getUserFromServer(String id, Subscriber<User> subscriber) {
        apiInterface.getUser(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public List<Class> getClassesFromRealm() {
        RealmResults<Class> classes = realm.where(Class.class).findAll();
        classes = classes.sort("timeFromH");
        List<Class> arrayListClasses = new ArrayList<>();
        for (Class c : classes) {
            arrayListClasses.add(c);
        }
        return arrayListClasses;
    }
}
