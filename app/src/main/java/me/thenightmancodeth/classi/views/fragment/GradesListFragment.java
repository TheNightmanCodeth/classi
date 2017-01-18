package me.thenightmancodeth.classi.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.Api;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.GradeType;
import me.thenightmancodeth.classi.views.ClassRecycleAdapter;
import me.thenightmancodeth.classi.views.GradeRecycleAdapter;

/**
 * Created by thenightman on 1/17/17.
 **/

public class GradesListFragment extends Fragment {
    @BindView(R.id.grades_list_rv) RecyclerView gradesRecycler;

    Realm realm;
    String className;
    Class thisClass;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grade_list_fragment, container, false);
        ButterKnife.bind(this, view);

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        
        className = getArguments().getString("class_name");
        thisClass = realm.where(Class.class).equalTo("name", className).findFirst();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        gradesRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        gradesRecycler.setLayoutManager(layoutManager);

        refreshGrades();
    }

    private List<Grade> getFakeGrades() {
        List<Grade> gradest = new ArrayList<>();
        for (int i = 10; i > 0; i--) {
            Grade newGrade = new Grade();
            newGrade.setGrade(10 * i);
            newGrade.setName("Grade " +i);
            newGrade.setDueDate("03/03/2017");
            newGrade.setDueTime("03:30 PM");
            newGrade.setDescription("This is the " +i +" grade");
            GradeType type = new GradeType();
            type.setPercent(50);
            type.setType("Type " +i);
            gradest.add(newGrade);
        }
        return gradest;
    }

    public List<Grade> getGradesFromRealm() {
        List<Grade> arrayListGrades = new ArrayList<>();
        for (Grade g : thisClas.getGrades()) {
                arrayListGrades.add(g);
        }
        return arrayListGrades;
    }

    public void refreshGrades() {
        GradeRecycleAdapter adapter = new GradeRecycleAdapter(getGradesFromRealm(),
                getContext());
        gradesRecycler.setAdapter(adapter);
    }
}
