package com.project.jvvas.moneyskills;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Training extends AppCompatActivity {

    // List of all fragments to open
    private Fragment[] myFrags = {new LetterQuiz() ,new AscendingNumbers(), new OddSymbol(), new MatchSymbols()};
    private String[] names = {"Anagrams Quiz", "Ascending Numbers", "Odd Symbol", "Match Symbols"};


    // Variables about the score.
    private TextView globalScoreView;
    private TextView gameName;
    private int globalScore;
    private int fragmentNo;

    private Button previousFrag;
    private Button nextFrag;
    private Button reset;
    private TextView scoreChange;

    private boolean canceled = false;

    private CountDownTimer timer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        //Getting the score text views
        globalScoreView = findViewById(R.id.global_score);
        gameName = findViewById(R.id.gameName);
        scoreChange = findViewById(R.id.score_change);

        previousFrag = findViewById(R.id.previous);
        nextFrag = findViewById(R.id.next);
        reset = findViewById(R.id.reset);

        fragmentNo = 0;
        openFragment(myFrags[fragmentNo]);
    }


    //Changed back button to close the app.
    @Override
    public void onBackPressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    //Method for calling on click for the buttons on the Odd character fragment
    public void checkCharacter(View v)
    {
        changeCurrentScore(((OddSymbol) myFrags[2]).checkCharacter(v));
    }

    //Method for calling on click for the buttons on the Ascending Numbers fragment
    public void buttonClicked(View v) {
        changeCurrentScore(((AscendingNumbers)myFrags[1]).buttonClicked(v));
    }

    //Method for calling on click for the buttons on Match Symbols fragment
    public void flipButton(View v)
    {
        int retValue = ((MatchSymbols)myFrags[3]).flipButton(v);
        if(retValue != 0)
        {
            changeCurrentScore(retValue);
        }
    }


    // ON CLICK METHODS FOR LETTER QUIZ
    public void onClickD(View v)
    {
        changeCurrentScore(((LetterQuiz) myFrags[0]).onClickD(v));
    }

    public void onClickC(View v)
    {
        changeCurrentScore(((LetterQuiz) myFrags[0]).onClickC(v));
    }

    public void onClickB(View v)
    {
        changeCurrentScore(((LetterQuiz) myFrags[0]).onClickB(v));
    }

    public void onClickA(View v)
    {
        changeCurrentScore(((LetterQuiz) myFrags[0]).onClickA(v));
    }



    //Method for opening a given fragment.
    private void openFragment(final Fragment fragment)   {
        globalScore = 0;
        globalScoreView.setText("Score:  " + globalScore);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.forFrag, fragment);
        fragmentTransaction.commit();

    }

    //Method for setting the timer to a Specific value
    /*
    private void setTimer(String counter)
    {
        timerText.setText(counter);
    }
    */
    //Method for changing the current score on all variables and Text Views
    private void changeCurrentScore(int scoreChange)
    {
        globalScore += scoreChange;
        globalScoreView.setText("Score:  " + globalScore);
        showScoreChange(scoreChange);
    }

    //Gives Score
    public int getScore()
    {
        return globalScore;
    }

    public void previousFrag(View v)
    {
        if (fragmentNo >0)
        {
            fragmentNo--;
            openFragment(myFrags[fragmentNo]);
            gameName.setText(names[fragmentNo]);
        }
    }


    public void nextFrag(View v)
    {
        if (fragmentNo < 3)
        {
            fragmentNo++;
            openFragment(myFrags[fragmentNo]);
            gameName.setText(names[fragmentNo]);
        }
    }

    public void reset(View v)
    {
        globalScore = 0;
        globalScoreView.setText("Score:  " + globalScore);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(myFrags[fragmentNo]);
        fragmentTransaction.attach(myFrags[fragmentNo]);
        fragmentTransaction.commit();
    }

    public void showScoreChange(int change)
    {
        if (timer != null)
        {
            canceled = true;
            timer.cancel();
        }
        if (change > 0)
        {
            scoreChange.setText("+" + String.valueOf(change));
            scoreChange.setTextColor(Color.rgb(0,200,0));
        }
        else if (change < 0 )
        {
            scoreChange.setText(String.valueOf(change));
            scoreChange.setTextColor(Color.rgb(200,0,0));
        }
        canceled = false;
        timer = new CountDownTimer(1100, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (!canceled)
                {
                    scoreChange.setText("");
                }
            }

        }.start();


    }
}