package me.thenightmancodeth.classi.models.data;

import io.realm.RealmObject;

/**
 * Created by thenightman on 1/8/17.
 */

public class GradeType extends RealmObject {
    private String type;
    private double percent;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
