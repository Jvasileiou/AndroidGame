package com.project.jvvas.moneyskills;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class AscendingNumbers extends Fragment {

    private int lastButton = 0;

    public AscendingNumbers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ascending_numbers, container, false);

        //Need to get an ArrayList of my buttons so I can randomly set numbers and colours.
        ArrayList<View> buttonsArray = rootView.getTouchables();

        //One array for having the colours for the button text and one to have the numbers I put on the them.
        ArrayList<Integer> numbers = new ArrayList<>();
        String[] colours = {"#256fe8", "#ef0000", "#000000", "#a807ed", "#f2df0e", "#e56f09"};
        for (int i = 0; i < 48; i++)
        {
            numbers.add(i + 1);
        }

        //Shuffle the numbers and randomly pick a colour for each button.
        Button but;
        Random index = new Random();
        int newIndex;
        Collections.shuffle(numbers);
        for (int i = 0; i < 48; i++)
        {
            newIndex = index.nextInt(6);
            but = (Button) buttonsArray.get(i);
            but.setText(String.valueOf(numbers.get(i)));
            but.setTextColor(Color.parseColor(colours[newIndex]));
        }
        return rootView;
    }

    //When a button is clicked check if it has value of the last button + 1. If it does need to increase score. Else decrease it.
    public int buttonClicked(View v) {
        final Button but = (Button) v;
        if (Integer.parseInt((String) but.getText()) == lastButton + 1) {
            but.setTextColor(Color.parseColor("#789376"));
            but.setEnabled(false);
            lastButton++;
            return 1;
        }
        else
        {
            return -1;
        }
    }

}