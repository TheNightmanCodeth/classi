package me.thenightmancodeth.classi.models.data;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by thenightman on 1/8/17.
 */

public class Class extends RealmObject {
    private String name;
    private String time;
    private String days;
    private String building;
    private String professor;
    private RealmList<Grade> grades;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public RealmList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(RealmList<Grade> grades) {
        this.grades = grades;
    }
}
