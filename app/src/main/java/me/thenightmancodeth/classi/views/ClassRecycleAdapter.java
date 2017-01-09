package me.thenightmancodeth.classi.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import io.realm.RealmList;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.GradeType;
import me.thenightmancodeth.classi.views.custom.CircleView;
import me.thenightmancodeth.classi.models.data.Class;

/**
 * Created by thenightman on 1/8/17.
 **/

public class ClassRecycleAdapter extends RecyclerView.Adapter<ClassRecycleAdapter.ClassViewHolder> {
    private List<Class> classes;
    private Context ctx;

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        public CircleView grade;
        public TextView title;
        public TextView timeDays;
        public TextView buildingProfessor;
        public ClassViewHolder(View listItemView) {
            super(listItemView);
            grade = (CircleView)listItemView.findViewById(R.id.grade);
            title = (TextView)listItemView.findViewById(R.id.title);
            timeDays = (TextView)listItemView.findViewById(R.id.date_time);
            buildingProfessor = (TextView)listItemView.findViewById(R.id.location_prof);
        }
    }

    public ClassRecycleAdapter(List<Class> classes, Context ctx) {
        this.classes = classes;
        this.ctx = ctx;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_list_item, parent, false);
        return new ClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        Class thisOne = classes.get(position);
        int grade = (int)average(thisOne.getGrades());
        holder.grade.setText(String.valueOf(grade));
        holder.grade.setBackgroundColor(genColor(grade));
        holder.title.setText(thisOne.getName());
        String timeDayString = thisOne.getTime() +" - " +thisOne.getDays();
        String buildingProfessorString = thisOne.getBuilding() +" - " +thisOne.getProfessor();
        holder.timeDays.setText(timeDayString);
        holder.buildingProfessor.setText(buildingProfessorString);
    }

    private int genColor(int grade) {
        if (grade >= 90) {
            return ContextCompat.getColor(ctx, R.color.a);
        } else if (grade >= 80) {
            return ContextCompat.getColor(ctx, R.color.b);
        } else if (grade >= 70) {
            return ContextCompat.getColor(ctx, R.color.c);
        } else if (grade >= 60) {
            return ContextCompat.getColor(ctx, R.color.d);
        } else {
            return ContextCompat.getColor(ctx, R.color.f);
        }
    }

    private double average(List<Grade> grades) {
        double sum = 0;
        int size = 0;
        for (Grade g: grades) {
            sum += g.getGrade() * g.getType().getPercent()/100;
            size++;
        }
        return sum/size;
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }
}
