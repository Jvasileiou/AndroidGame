package com.project.jvvas.moneyskills;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchSymbols extends Fragment {

    private String[] characters = new String[30];
    private HashMap<Button, String> buttonSymbols= new HashMap<Button, String>();
    private ArrayList<View> buttonsArray;
    private Button previousFlip;


    private ArrayList<Button> firstFlips = new ArrayList<Button>();
    private ArrayList<Button> secondFlips = new ArrayList<Button>();
    private int buttonPair;

    public MatchSymbols() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_match_symbols, container, false);
        buttonsArray = rootView.getTouchables();
        Collections.shuffle(buttonsArray);
        int counter = 0;
        for (char ch = 'a'; ch<= 'o'; ++ch)
        {
            for (int i=0; i < 2; i++)
            {
                characters[counter]= String.valueOf(ch);
                counter++;
            }
        }

        for (int i=0; i<30; i++ )
        {
            Button but = (Button) buttonsArray.get(i);
            buttonSymbols.put(but,characters[i]);
            but.setText(characters[i]);
            but.setEnabled(false);
        }

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                for (View button: buttonsArray)
                {
                    Button but = (Button) button;
                    hide(but);
                }
            }

        }.start();
        return rootView;
    }

    public void hide(Button but)
    {
        but.setEnabled(true);
        but.setText("?");
    }

    public int flipButton(View v)
    {
        final Button but = (Button) v;
        if (firstFlips.size() == secondFlips.size()) {
            firstFlips.add(but);
            but.setText(buttonSymbols.get(but));
            but.setEnabled(false);
        } else if (firstFlips.size() == secondFlips.size() + 1) {
            secondFlips.add(but);
            but.setText(buttonSymbols.get(but));
            but.setEnabled(false);
        }
        return checkpair();
    }

    private int checkpair()
    {
        if (secondFlips.size()>buttonPair)
        {

            if ((buttonSymbols.get(firstFlips.get(buttonPair))).equals(buttonSymbols.get(secondFlips.get(buttonPair))))
            {
                buttonPair++;
                return 1;
            }
            else
            {
                final int buttToHide = buttonPair;
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {}

                    public void onFinish()
                    {
                        hide(firstFlips.get(buttToHide));
                        hide(secondFlips.get(buttToHide));
                    }
                }.start();
                buttonPair++;
                return 0;
            }
        }
        return 0;
    }
}