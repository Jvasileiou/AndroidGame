package com.project.jvvas.moneyskills;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinalScore extends Fragment {


    public FinalScore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_final_score, container, false);
        return rootView;
    }

    //Method for exit button
    public void ExitBut(View v) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}