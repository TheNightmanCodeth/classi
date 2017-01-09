package me.thenightmancodeth.classi.models.data;

import io.realm.RealmObject;

/**
 * Created by thenightman on 1/8/17.
 */

public class Grade extends RealmObject {
    private String name;
    private String description;
    private GradeType type;
    private double grade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GradeType getType() {
        return type;
    }

    public void setType(GradeType type) {
        this.type = type;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
