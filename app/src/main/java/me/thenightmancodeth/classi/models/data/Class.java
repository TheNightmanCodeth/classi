package me.thenightmancodeth.classi.models.data;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by thenightman on 1/8/17.
 */

public class Class extends RealmObject {
    private String name;
    private int timeFromH;
    private int timeFromM;
    private int timeToH;
    private int timeToM;
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

    public int getTimeFromH() {
        return timeFromH;
    }

    public void setTimeFromH(int timeFromH) {
        this.timeFromH = timeFromH;
    }

    public int getTimeFromM() {
        return timeFromM;
    }

    public void setTimeFromM(int timeFromM) {
        this.timeFromM = timeFromM;
    }

    public int getTimeToH() {
        return timeToH;
    }

    public void setTimeToH(int timeToH) {
        this.timeToH = timeToH;
    }

    public int getTimeToM() {
        return timeToM;
    }

    public void setTimeToM(int timeToM) {
        this.timeToM = timeToM;
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
