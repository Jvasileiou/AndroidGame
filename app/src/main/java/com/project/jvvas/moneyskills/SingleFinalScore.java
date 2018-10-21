package com.project.jvvas.moneyskills;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.games.Game;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleFinalScore extends Fragment {

    //Score variables to update base and give rewards
    private int score;
    private int highscore;

    //Text views
    private TextView scoreReview;
    private TextView rewards;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private FirebaseAuth mAuth;
    private ValueEventListener listener;

    public SingleFinalScore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_final_score, container, false);

        score = ((GameActivity)getActivity()).getScore();
        scoreReview = rootView.findViewById(R.id.scoreReview);
        rewards =  rootView.findViewById(R.id.rewards);


        mAuth = FirebaseAuth.getInstance();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                highscore = dataSnapshot.child("highscore").getValue(Integer.class);
                if (score > highscore)
                {
                    scoreReview.setText(String.format("Congratulations! New Highscore of %d points!", score));
                    mRootReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("highscore").setValue(score);
                }
                else if (score == highscore)
                {
                    scoreReview.setText(String.format("Great! You equaled your Highscore of %d points!", score));
                }
                else
                {
                    scoreReview.setText(String.format("You were %d point(s) below your best! You can do it next time!", (highscore-score)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRootReference.child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(listener);

        return rootView;
    }

    public void MainMenu(View v)
    {
        Intent accountIntent = new Intent(getActivity(),SingleMultiPlayer.class);
        startActivity(accountIntent);
    }

}
