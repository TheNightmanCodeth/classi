package me.thenightmancodeth.classi.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.thenightmancodeth.classi.R;

/**
 * Created by thenightman on 1/17/17.
 **/

public class GradesListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grade_list_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
