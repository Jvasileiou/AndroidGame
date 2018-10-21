package com.project.jvvas.moneyskills;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class OddSymbol extends Fragment {

    private ArrayList<View> buttonsArray;
    private String correctButton;
    private ArrayList<String[]> letters = new ArrayList<String[]>();
    private String wrongButton;
    private int indexCounter = 0;

    public OddSymbol() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_odd_symbol, container, false);

        //Fill the ArrayList with the buttons and then fill them(text/colour).
        buttonsArray = rootView.getTouchables();
        fillArrayList();
        fillButtons();

        return rootView;
    }


    //Randomly picks a button for the different character and sets text and colour randomly picked from the Array and ArrayList.
    public void fillButtons()
    {
        String[] colours = {"#256fe8", "#ef0000", "#000000", "#a807ed", "#f2df0e", "#e56f09"};
        Button but;
        Random index = new Random();
        int newIndex;
        //Pick the characters for the buttons.
        correctButton = letters.get(indexCounter)[1];
        wrongButton = letters.get(indexCounter)[0];
        // Sets button text and colour.
        for (View button:buttonsArray)
        {
            newIndex = index.nextInt(6);
            but = (Button) button;
            but.setText(wrongButton);
            but.setTextColor(Color.parseColor(colours[newIndex]));
        }
        newIndex = index.nextInt(buttonsArray.size());
        but = (Button) buttonsArray.get(newIndex);
        but.setText(correctButton);
        indexCounter++;
        if (indexCounter == letters.size())
        {
            indexCounter = 0;
        }
    }


    //Checks if the button clicked is the one that has the different character. Refills the buttons and returns the needed score change.
    public int checkCharacter(View v)
    {
        final Button but = (Button) v;
        if (but.getText().equals(correctButton))
        {
            fillButtons();
            return 1;
        }
        else
        {
            fillButtons();
            return -1;
        }
    }

    private void fillArrayList()
    {

        letters.add(new String[] {"G","C"});
        letters.add(new String[] {"R","P"});
        letters.add(new String[] {"E","F"});
        letters.add(new String[] {"D","O"});
        letters.add(new String[] {"1","7"});
        letters.add(new String[] {"V","U"});
        letters.add(new String[] {"M","N"});
        letters.add(new String[] {"8","9"});

        letters.add(new String[] {"C","G"});
        letters.add(new String[] {"P","R"});
        letters.add(new String[] {"F","E"});
        letters.add(new String[] {"O","D"});
        letters.add(new String[] {"7","l"});
        letters.add(new String[] {"U","V"});
        letters.add(new String[] {"N","M"});
        letters.add(new String[] {"9","8"});

        letters.add(new String[] {"O","0"});

        letters.add(new String[] {"9","6"});
        letters.add(new String[] {"6","9"});


        Collections.shuffle(letters);
    }

}