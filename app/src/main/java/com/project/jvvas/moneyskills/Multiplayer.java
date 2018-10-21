package com.project.jvvas.moneyskills;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class Multiplayer extends AppCompatActivity{


    // List of all fragments to open
    private Fragment[] myFrags = {new LetterQuiz() ,new AscendingNumbers(), new OddSymbol(), new MatchSymbols(), new SingleFinalScore()};


    // Variables about the score.
    private TextView globalScoreView;
    private TextView scoreView;
    private int globalScore;
    private int score;

    // Possition Text view
    private TextView pos;


    // Variables about the timer and the countdown
    private TimerTask task1;
    private TextView timerText;
    private ProgressBar pb;
    private Countdown countdownTimer;
    private boolean onTimer = false;
    private boolean nextTimer = true;

    // Fields about Firebase references and player id/table
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference tableRef;
    private String playerID;
    private String opponentID;
    private int playerNo;
    private String table = "table2";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        playerID = getIntent().getStringExtra("playerID");
        opponentID = getIntent().getStringExtra("opponentID");
        playerNo = getIntent().getIntExtra("playerNo",0);

        //Text view for possition
        pos = findViewById(R.id.position);

        //Getting the score text views
        globalScoreView = findViewById(R.id.global_score);
        scoreView = findViewById(R.id.game_score);

        //Getting the 'timer' ready
        pb = findViewById(R.id.timer);
        timerText = findViewById(R.id.textTimer);
        pb.setRotation(270);
        changeBar(0);

        // FIREBASE   NEED TO CHANGE FOR NEW TABLES
        tableRef = mRootReference.child("Game").child("Tables").child(playerID + "-" + opponentID);


        //TIMER TASKS
        new Timer().scheduleAtFixedRate(task1 = new TimerTask() {
            int counter = 0;
            int fragNo = 0;
            @Override
            public void run() {
                //Increase timer every second, prepare for next countdown and reduce timer to 0 again
                if (pb.getProgress() == pb.getMax())
                {
                    counter = 0;
                }
                else if (pb.getProgress() > 0 )
                {
                    counter++;
                    if (pb.getProgress() == 20 )
                    {
                        nextTimer = true;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Start next countdown if timer is at 0 again, and we have another game.
                        if ((!onTimer) && pb.getProgress() == 0 && !(fragNo==myFrags.length-1) && nextTimer)
                        {
                            countdownTimer = new Countdown();
                            openFragment(countdownTimer);
                            onTimer = true;
                            nextTimer = false;
                        }
                        //Open next game when the countdown has finished, open the last fragment which has no game.
                        if ((countdownTimer.getFinished() && onTimer) || (pb.getProgress() == 0 &&(fragNo==myFrags.length-1)))
                        {
                            onTimer = false;
                            if (fragNo==myFrags.length-1)
                            {
                                task1.cancel();
                                counter=-1;
                            }
                            openFragment(myFrags[fragNo]);
                            fragNo++;
                            counter++;
                        }
                        changeBar(counter);
                    }
                });
            }
        }, 0, 1000);
        // END OF TIMER TASKS

//        // FIREBASE======================================================================================================
//        tableRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int position = 1;
//                int currScore;
//                for (DataSnapshot snapshot:  dataSnapshot.getChildren())
//                {
//                    currScore =  snapshot.child("score").getValue(Integer.class);
//                    if (currScore > globalScore)
//                    {
//                        position++;
//                    }
//                }
//                pos.setText(Integer.toString(position));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
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


    //Method for the exit button on the last fragment
    public void MainMenu(View v) {
        ((SingleFinalScore) myFrags[4]).MainMenu(v);
        finish();
    }

    //Method for opening a given fragment.
    private void openFragment(final Fragment fragment)   {
        score = 0;
        scoreView.setText(String.valueOf(score));
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.forFrag, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //Method for setting the timer to a Specific value
    private void changeBar(int prog)
    {
        pb.setProgress(prog);
        //setTimer(String.valueOf(prog));
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
        score += scoreChange;
        globalScore += scoreChange;
        globalScoreView.setText("Score:  " + globalScore);
        scoreView.setText(String.valueOf(score));
        fireScore(globalScore);
    }

    //Gives Score
    public int getScore()
    {
        return globalScore;
    }



    //Method to set the score of this player in firebase.
    public void fireScore(int score)
    {
        if (playerNo == 1)
        {
            tableRef.child("player1").child("score").setValue(score);
        }
        else if (playerNo == 2)
        {
            tableRef.child("player2").child("score").setValue(score);
        }

    }

}
