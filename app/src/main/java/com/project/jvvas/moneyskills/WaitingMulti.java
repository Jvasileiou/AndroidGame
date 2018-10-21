package com.project.jvvas.moneyskills;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.games.Game;
import com.google.android.gms.games.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mopub.common.DiskLruCache;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WaitingMulti extends AppCompatActivity {

    //Firebase instances/references.
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference myPLayerRef;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Info about the current player.
    private String playerID = "playerID2";
    private boolean readyForGame = false;

    //Info about the table
    private String table = "table2";
    private String testTable = "table";
    private int tableCount;
    private int currTableSeats;
    private String opponentID;
    private String opponentPush;
    private int playerNo;
    private CountDownTimer counter;
    private PlayerForBase player1;
    // Listener for when another player in the table gets ready.
    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_multi);
        /*counter = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish()
            {
                if (player1.gameReady)
                {
                    //startGame();
                }
            }

        }.start();*/
        playerID = mAuth.getCurrentUser().getUid();
        searchForOpponent();

    }



    public void writePlayerWaiting(String opponent)
    {
        player1 = new PlayerForBase(1, opponent, true);
        mRootReference.child("Game").child("LookingForGame").child(playerID).setValue(player1);
        playerNo = 1;
        makeTable();
        startGame();
    }

    public void writePlayerWaiting()
    {
        final PlayerForBase player2 = new PlayerForBase(2, "none", false);
        mRootReference.child("Game").child("LookingForGame").child(playerID).setValue(player2);
        playerNo = 2;
        mRootReference.child("Game").child("LookingForGame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                opponentID = dataSnapshot.child(playerID).child("opponentID").getValue(String.class);
                if (opponentID != "none")
                {
                    if (playerID == dataSnapshot.child(opponentID).child("opponentID").getValue(String.class)  && !dataSnapshot.child(playerID).child("gameReady").getValue(boolean.class))
                    {
                        mRootReference.child("Game").child("LookingForGame").child(playerID).child("gameReady").setValue(true);
                        startGame();
                    }
                    else
                    {
                        mRootReference.child("Game").child("LookingForGame").child(playerID).child("gameReady").setValue(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void searchForOpponent()
    {
        mRootReference.child("Game").child("LookingForGame").orderByChild("opponentID").equalTo("none").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    opponentID = snapshot.getKey();
                }
                if (opponentID != null)
                {
                    mRootReference.child("Game").child("LookingForGame").child(opponentID).child("opponentID").setValue(playerID);
                    writePlayerWaiting(opponentID);
                }
                else
                {
                    writePlayerWaiting();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void makeTable()
    {
        if (playerNo == 1)
        {
            mRootReference.child("Game").child("Tables").child(playerID + "-" + opponentID).child("player1").child("score").setValue(0);
            mRootReference.child("Game").child("Tables").child(playerID + "-" + opponentID).child("player2").child("score").setValue(0);
        }
    }

    public void startGame()
    {
        Intent intent = new Intent(WaitingMulti.this,Multiplayer.class);
        intent.putExtra("playerID", playerID);
        intent.putExtra("opponenetID", opponentID);
        intent.putExtra("playerNo", playerNo);
        startActivity(intent);
        finish();
    }

}