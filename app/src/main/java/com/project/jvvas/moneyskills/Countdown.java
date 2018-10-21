package com.project.jvvas.moneyskills;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Countdown extends Fragment {

    private TextView counter;
    private boolean finished = false;

    public Countdown() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_countdown, container, false);

        // Sets every second a text view to the timers value.
        counter = rootView.findViewById(R.id.counter);
        new CountDownTimer(3400, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText(""+ millisUntilFinished / 1000);
            }

            public void onFinish() {
                finished = true;
            }

        }.start();
        return rootView;
    }

    // When timer is finished need to notify the activity to start next game fragment.
    public boolean getFinished()
    {
        return finished;
    }

}