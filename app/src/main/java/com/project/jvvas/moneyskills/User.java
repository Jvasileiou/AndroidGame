package com.project.jvvas.moneyskills;

public class User
{
    public  String fullName , email ;
    public  int
            highscore = 0 ,
            coins = 0 ,
            numberOfHearts = 6 , // The MAX value
            numberOfGames =  0 ; // How many games the user has played so far


    public User()
    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String name, String email )
    {
        this.fullName = name ;
        this.email = email ;
    }
}
