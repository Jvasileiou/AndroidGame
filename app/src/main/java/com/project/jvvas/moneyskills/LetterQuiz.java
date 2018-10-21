package com.project.jvvas.moneyskills;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class LetterQuiz extends Fragment {

    static Random rnd = new Random();

    private String displayedWord;
    private int correctPositionAnswer;
    private int counterWord = 0;
    View v;

    private final String allWordsArray[] = {"EXIST", "APPLE", "LION" , "SWORD" , "TENNIS" , "POTATO" , "MOUSE" , "KING" , "QUEEN" , "VEHICLE",
            "CRAFT", "WARRIOR", "FRAME", "PICTURE", "OXYGEN", "DRAGON","CREAM", "FLASH", "BUCKET", "STRAW" , "CIRCLE" , "DEALER" , "DOMAIN" ,
            "SELECT" , "BELOW" , "TASTE" , "STUDY" , "SILENT" , "PIZZA" , "ACTIVE" , "ASPECT" , "BELONG" , "BEAUTY" , "DRIVER" , "ETHNIC" ,
            "FACTOR" , "FINGER" , "FATHER" , "GATHER" , "GARDEN" , "ISLAND" , "LEADER" , "JUNIOR" , "MARKET" , "MUSEUM" , "NUMBER" , "PHRASE" ,
            "SCHEME" , "SOCIAL" , "SLIGHT" ,  "STRAIN" , "SURVEY" , "TRAVEL" , "UPDATE" , "UNIQUE" , "VISION" , "VISUAL" , "WEIGHT" , "TWENTY" ,
            "WALKER" , "WORKER" , "WINTER" , "SUMMER"  , "YELLOW" , "TOWARD" , "WRITER" , "INSIDE" , "LAWYER" , "LEGACY" , "BOTTLE" , "BATTLE" ,
            "ACTOR" , " AMONG" , "ALONE" , " ANGLE" , "ARRAY" , "AUDIO" , "BLACK" , "CHECK" , "CRIME" , "FAITH" , "FORCE" , "CROWD" , "FIFTH" ,
            "HEART" , "HORSE" , "INDEX" , "GUESS" , "LIGHT" , "PLAIN" , "POWER" , "PRIZE" , "AWARD" , "SPACE" , "SPARE" , "WORLD" , "WRONG" ,
            "WORSE" , "WHOSE" , "WRONG" , "WASTE" , "YOUNG" , "YOUTH" , "WATCH" , "WHICH" , "THEME" , "TOWER" , "UNTIL" , "SHELL" , "SHIFT"
    } ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v  = inflater.inflate(R.layout.fragment_letter_quiz, container, false);

        // Shuffle the array  with the words
        Collections.shuffle(Arrays.asList(allWordsArray));

        startUp();
        return v;

    }


    public int onClickD(View v) {
        if(correctPositionAnswer==4){
            startUp();
            return 1;
        }
        else
        {
            startUp();
            return -1;
        }
    }

    public int onClickC(View v) {
        if(correctPositionAnswer==3){
            startUp();
            return 1;
        }
        else
        {
            startUp();
            return -1;
        }
    }

    public int onClickB(View v) {
        if(correctPositionAnswer==2){
            startUp();
            return 1;
        }
        else
        {
            startUp();
            return -1;
        }
    }

    public int onClickA(View v) {
        if(correctPositionAnswer==1){
            startUp();
            return 1;
        }
        else
        {
            startUp();
            return -1;
        }
    }


    public void startUp() {

        this.displayedWord = allWordsArray[counterWord];
        TextView theWord = v.findViewById(R.id.theWord);
        TextView firstAnswer = v.findViewById(R.id.firstAnswer);
        TextView secondAnswer = v.findViewById(R.id.secondAnswer);
        TextView thirdAnswer = v.findViewById(R.id.thirdAnswer);
        TextView fourthAnswer = v.findViewById(R.id.fourthAnswer);
        TextView Atext = v.findViewById(R.id.Atext);
        TextView Btext = v.findViewById(R.id.Btext);
        TextView Ctext = v.findViewById(R.id.Ctext);
        TextView Dtext = v.findViewById(R.id.Dtext);

        TextView[] textsArray = {firstAnswer, secondAnswer, thirdAnswer, fourthAnswer};
        TextView[] lettersText = {Atext, Btext, Ctext, Dtext};

        //make buttons
        ImageButton buttonA = v.findViewById(R.id.buttonA);
        ImageButton buttonB = v.findViewById(R.id.buttonB);
        ImageButton buttonC = v.findViewById(R.id.buttonC);
        ImageButton buttonD = v.findViewById(R.id.buttonD);


        theWord.setText(displayedWord);
        int[] colorArray = getColorArray();
        String[] fourWords = getFourWords();

        counterWord++;

        if (counterWord == 19) {
            counterWord = 0;
        }

        for (int i = 0; i < 4; i++) {
            textsArray[i].setTextColor(colorArray[i]);
            textsArray[i].setText(fourWords[i]);
            lettersText[i].setTextColor(colorArray[i]);
        }
    }


    public int[] getColorArray(){
        int c1 = getResources().getColor(R.color.color1);
        int c2 = getResources().getColor(R.color.color2);
        int c3 = getResources().getColor(R.color.color3);
        int c4 = getResources().getColor(R.color.color4);
        int[] ar = {c1, c2, c3, c4};
        //shuffle
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }


    public String[] getFourWords(){
        int counter = 0;
        int counter4random = 0;
        String[] fourWords = {"", "", "", ""};

        String correctWord = makeCorrectWord();

        String[] randomWords = makeAllRandomWords();

        this.correctPositionAnswer = rnd.nextInt(4)+1;
        fourWords[correctPositionAnswer-1] = correctWord;
        while(counter < 4){
            if(fourWords[counter].equals(correctWord)){
                if(counter==3){
                    break;
                }
                else {
                    counter++;
                }
            }
            fourWords[counter] = randomWords[counter4random];
            counter4random++;
            counter++;
        }
        return fourWords;
    }

    public String makeCorrectWord(){
        String emptyString;
        while(true) {
            emptyString = "";
            char[] currentWordArray = displayedWord.toCharArray();

            for (int i = displayedWord.length() - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                char tempChar = currentWordArray[index];
                currentWordArray[index] = currentWordArray[i];
                currentWordArray[i] = tempChar;
            }
            emptyString = makeString(currentWordArray);
            if(emptyString.equals(displayedWord)){
                continue;
            }
            break;

        }

        return emptyString;
    }

    public String[] makeAllRandomWords(){
        String[] twoRandomWords = makeTwoRandomWords();
        String[] allWords = new String[3];
        allWords[0] = twoRandomWords[0];
        allWords[1] = twoRandomWords[1];
        allWords[2] = makeOneRandomWord();
        return allWords;

    }

    public String makeOneRandomWord(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] currentWordArray = displayedWord.toCharArray();
        char newCharacter = alphabet.charAt(rnd.nextInt(26));
        int oldCharacterPos = rnd.nextInt(displayedWord.length());
        while (currentWordArray[oldCharacterPos] == newCharacter)
        {
            newCharacter = alphabet.charAt(rnd.nextInt(26));
        }
        currentWordArray[oldCharacterPos] = newCharacter;
        String finalWord = makeString(currentWordArray);
        System.out.println("2 "+finalWord + " " + displayedWord);
        if(finalWord.length()==3){makeOneRandomWord();}
        return finalWord;
    }

    public String[] makeTwoRandomWords(){
        String[] randomWords = new String[2];
        int counter = 0;

        while(counter<2) {
            String oneWord = makeCorrectWord();
            char[] oneWordArray = oneWord.toCharArray();
            int rnd1 = rnd.nextInt(displayedWord.length()), rnd2 = rnd.nextInt(displayedWord.length());
            char char1 = oneWordArray[rnd1];
            char char2 = displayedWord.charAt(rnd2);

            while (char1 == char2) {
                rnd1 = rnd.nextInt(displayedWord.length());
                rnd2 = rnd.nextInt(displayedWord.length());
                char1 = oneWordArray[rnd1];
                char2 = displayedWord.charAt(rnd2);
            }
            oneWordArray[rnd1] = char2;
            oneWord = makeString(oneWordArray);
            randomWords[counter] = oneWord;
            counter++;

        }
        return randomWords;

    }

    public String makeString(char[] c){
        String word = "";
        for(char character : c){
            if(character==' '){
                continue;
            }
            word += Character.toString(character);
        }
        return word;
    }



}