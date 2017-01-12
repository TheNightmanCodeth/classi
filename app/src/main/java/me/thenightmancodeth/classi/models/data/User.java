package me.thenightmancodeth.classi.models.data;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by thenightman on 1/8/17.
 **/

public class User extends RealmObject {
    private String name;
    private String email;
    private RealmList<Class> classes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<Class> getClasses() {
        return classes;
    }

    public void setClasses(RealmList<Class> classes) {
        this.classes = classes;
    }
}
