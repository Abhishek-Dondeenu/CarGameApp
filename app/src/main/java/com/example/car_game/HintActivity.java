package com.example.car_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class HintActivity extends AppCompatActivity {
    private String[] carImages = {"audi_1", "audi_2", "audi_3", "audi_4", "audi_5", "mercedes_1", "mercedes_2", "mercedes_3", "mercedes_4", "mercedes_5", "bmw_1", "bmw_2", "bmw_3", "bmw_4", "bmw_5", "ford_1", "ford_2", "ford_3", "ford_4", "ford_5", "honda_1", "honda_2", "honda_3", "honda_4", "toyota_1", "toyota_2", "toyota_3", "toyota_4", "toyota_5", "toyota_6"};// an array of image names
    private String carMakeTofind;//the make of the car to be guessed
    private char[] carMakeFound;// the car make name found stored in a char array to show progression of user
    private String imageName;//the name of the image
    public static final Random random_image = new Random();
    private int numberOfAttemptsUsed;
    private ArrayList<String> letters = new ArrayList<>();//arraylist that contains letters already entered by the user
    public static final int attemptLimit = 3;
    public int time;//time
    private String switchMode = "";
    public static final String EXTRA_MESSAGE2 = "com.example.car_game.MESSAGE";
    private ImageView randomImageView;
    private Button submit_button;
    private TextView dashText;//contains the dashes
    private EditText guessLetter;//contains the given letter
    private String givenLetter;//giver letter in string
    private TextView timerText;//contains the timer
    private Toast messagetoast;
    private String displayText;
    private TextView correct_text;
    private CountDownTimer ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        randomImageView = findViewById(R.id.hint_image_view);
        time = 20;
        numberOfAttemptsUsed = 0;
        Intent intent = getIntent();
        switchMode = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        timerText = findViewById(R.id.hint_timer);

        //code reference at https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android/10032406#10032406
        ct= new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Timer: " + checkDigit(time) + " s");
                if (time != 0) {
                    time--;
                } else {
                    cancel();
                }
            }

            public void onFinish() {
                timerText.setText("Timer: " + checkDigit(time) + " s");
                timerCondition();

            }
        };
        hintGameStart();//start the hint game
    }

    //this method modifies the default back in the android app in order stop to the timer manually
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            time = 0;
            //stops the timer when the default button is pressed
            if (switchMode.equals("on")) {
                ct.cancel();
            }

            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void gameTwoInitialize() {
        timerText = findViewById(R.id.hint_timer);
        randomImageView = findViewById(R.id.hint_image_view);
        Resources res = getResources();
        int resID = res.getIdentifier(imageName, "drawable", getPackageName());
        randomImageView.setImageResource(resID);
        if (switchMode.equals("on")) {
            timerText.setText("Timer: " + checkDigit(time) + " s");
        }
    }

    //this method prevents the activity from restarting when the orientation is changed and display current images,time etc
    //code referred at https://stackoverflow.com/questions/456211/activity-restart-on-rotation-android
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_hint);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gameTwoInitialize();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gameTwoInitialize();

        }
    }

    //the start method that takes a random image and the following dashes for the car make name will appear initially
    public void hintGameStart() {
        letters.clear();


        //takes the string element from carImages array and is converted into drawable resource id to put images in the imageview
        imageName = nextImage();//gets a random image name
        Resources res = getResources();
        int resID = res.getIdentifier(imageName, "drawable", getPackageName());
        randomImageView.setImageResource(resID);

        TextView text = findViewById(R.id.dash_text_view);
        carMakeTofind = imageName.substring(0, imageName.indexOf('_')).toLowerCase();

        //the intialisation of the given word
        carMakeFound = new char[carMakeTofind.length()];

        //converts the letters in a given word to dashes
        for (int i = 0; i < carMakeFound.length; i++) {
            carMakeFound[i] = '-';

        }
        //converts the car make char value to a string in order to set it to the text box
        String d = String.valueOf(carMakeFound);
        text.setText(d);

        //checks if the user has enabled the timer
        if (switchMode.equals("on")) {
            ct.start();
        }


    }

    //the submit guess mehthod for the hints game
    public void submitGuess(View view) {
        time=20;
        submit_button = findViewById(R.id.submit_button);
        carMakeTofind = imageName.substring(0, imageName.indexOf('_')).toUpperCase();
        dashText = findViewById(R.id.dash_text_view);
        guessLetter = findViewById(R.id.hint_answer);
        givenLetter = guessLetter.getText().toString().toUpperCase();//gets the user input


        if (submit_button.getTag().equals("next")) {
            Intent nextHintIntent = new Intent(HintActivity.this, HintActivity.class);
            nextHintIntent.putExtra(EXTRA_MESSAGE2, switchMode);
            finish();
            startActivity(nextHintIntent);

        } else if (givenLetter.matches("")) {//checks if no letter is entered
            messagetoast = Toast.makeText(this,
                    "You have to enter a letter",
                    Toast.LENGTH_SHORT);
            messagetoast.show();
        } else {

            // updates the letters found in the give word
            enterLetter(givenLetter);
            String listString = "";

            // concatenates the elements in the letters arraylist in to a string to show letters
            for (String letter : letters) {
                listString += letter;
            }

            StringBuilder stringBuilder = new StringBuilder(listString);
            String mainWord = carMakeTofind.replaceAll("[^" + stringBuilder + "]", "-").toUpperCase();//removes the dash character and replaces with the letter given that is in the car name
            dashText.setText(mainWord);//adds the letters and the remaining dashes for a particular car name in the textView element
            guessLetter.setText("");//leaves the textview blank for the user enter a new letter

            // checks if the given is word is correct
            if (mainWord.equals(carMakeTofind)) {
                String checkedMark = "\u2713";
                messagetoast = Toast.makeText(this,
                        "Correct! " + checkedMark,
                        Toast.LENGTH_SHORT);
                messagetoast.getView().setBackgroundColor(Color.GREEN);
                messagetoast.show();

                //submit button changes
                submit_button.setText(R.string.next_button_text);
                submit_button.setTag("next");
                submit_button.setEnabled(true);
                guessLetter.setEnabled(false);
                time = 0;
                if (switchMode.equals("on")) {
                    ct.cancel();
                    ct.start();
                }



            }
            if (!carMakeTofind.contains(givenLetter)) {
                messagetoast = Toast.makeText(this,
                        "Number of attempts left: " + (attemptLimit - numberOfAttemptsUsed),//displays the attempts remaining
                        Toast.LENGTH_SHORT);

                messagetoast.show();
                messagetoast.setGravity(Gravity.TOP, 0, 0);
                time = 20;//resets the timer after each attempt
                if (switchMode.equals("on")) {
                    ct.cancel();
                    ct.start();
                }

            }

            //once the user run of attempts, he will get a message that he got his answer wrong and gets to see the correct answer
            if (numberOfAttemptsUsed == attemptLimit) {
                String crossMark = "\u2716";
                messagetoast = Toast.makeText(this,
                        "Wrong! " + crossMark,
                        Toast.LENGTH_SHORT);
                messagetoast.getView().setBackgroundColor(Color.RED);
                messagetoast.show();


                correct_text = findViewById(R.id.hint_correct_name_text);
                displayText = "Correct Answer: " + carMakeTofind.toUpperCase();// contains the correct answer
                correct_text.setText(displayText);
                correct_text.setBackgroundColor(Color.YELLOW);

                submit_button.setText(R.string.next_button_text);
                submit_button.setTag("next");
                submit_button.setEnabled(true);

                guessLetter.setEnabled(false);
                time=0;
                if (switchMode.equals("on")) {
                    ct.cancel();
                    ct.start();
                }



            }

        }


        //this lets the user to play the hint game again


    }

    //this method gets the user entered letter and checks if that letter is in the word to be found
    //code referenced at https://stackoverflow.com/questions/4988050/java-indexof-method-for-multiple-matches-in-string
    public void enterLetter(String givenLetter) {
        // checks if the car make name to find contains the given letter
        if (carMakeTofind.contains(givenLetter)) {
            // if so, the "-" character will replaced by the character c
            int index = carMakeTofind.indexOf(givenLetter);

            while (index >= 0) {
                carMakeFound[index] = carMakeTofind.charAt(0);
                index = carMakeTofind.indexOf(carMakeTofind, index + 1);
            }
        } else {
            // given letter not in the word will reduce the attempts available by 1
            numberOfAttemptsUsed++;
        }
        // the given letter is add to the letters arrayList
        letters.add(givenLetter);


    }

    //returns a random image
    private String nextImage() {
        return carImages[random_image.nextInt(carImages.length)];

    }

    //converts the int data in to double digit
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }






    //method used when the timer counts to zero
    public void timerCondition() {
        submit_button = findViewById(R.id.submit_button);
        carMakeTofind = imageName.substring(0, imageName.indexOf('_')).toUpperCase();
        dashText = findViewById(R.id.dash_text_view);
        guessLetter = findViewById(R.id.hint_answer);
        givenLetter = guessLetter.getText().toString().toUpperCase();//gets the user input


        if (givenLetter.matches("")) {//checks if no letter is entered
            numberOfAttemptsUsed++;
            messagetoast = Toast.makeText(this,
                    "Number of attempts left: " + (attemptLimit - numberOfAttemptsUsed),
                    Toast.LENGTH_SHORT);
            messagetoast.show();
            messagetoast.setGravity(Gravity.TOP, 0, 0);
            time = 20;
            ct.cancel();
            ct.start();

        } else {
            //updates the letters found in the give word
            enterLetter(givenLetter);
            String listString = "";
            // concatenates the elements in the letters arraylist in to a string to show letters
            for (String letter : letters) {
                listString += letter;
            }

            StringBuilder stringBuilder = new StringBuilder(listString);
            String mainWord = carMakeTofind.replaceAll("[^" + stringBuilder + "]", "-").toUpperCase();//removes the dash character and replaces with the letter given that is in the car name
            dashText.setText(mainWord);//adds the letters and the remaining dashes for a particular car name in the textView element
            guessLetter.setText("");//leaves the textview blank for the user enter a new letter

            // checks if the given word is correct
            if (mainWord.equals(carMakeTofind)) {
                String checkedMark = "\u2713";
                messagetoast = Toast.makeText(this,
                        "Correct! " + checkedMark,
                        Toast.LENGTH_SHORT);
                messagetoast.getView().setBackgroundColor(Color.GREEN);

                messagetoast.show();

                //submit button changes
                submit_button.setText(R.string.next_button_text);
                submit_button.setTag("next");
                submit_button.setEnabled(true);
                guessLetter.setEnabled(false);


            }
            if (!carMakeTofind.contains(givenLetter)) {
                //displays the number of attempts left
                messagetoast = Toast.makeText(this,
                        "Number of attempts left: " + (attemptLimit - numberOfAttemptsUsed),
                        Toast.LENGTH_SHORT);

                messagetoast.show();
                messagetoast.setGravity(Gravity.TOP, 0, 0);
                time = 20;
                ct.cancel();
                ct.start();


            }
            if (numberOfAttemptsUsed == attemptLimit) {
                String crossMark = "\u2716";
                messagetoast = Toast.makeText(this,
                        "Wrong! " + crossMark,
                        Toast.LENGTH_SHORT);
                messagetoast.getView().setBackgroundColor(Color.RED);
                messagetoast.show();

                correct_text = findViewById(R.id.hint_correct_name_text);
                displayText = "Correct Answer: " + carMakeTofind.toUpperCase();// contains the correct answer
                correct_text.setText(displayText);
                correct_text.setBackgroundColor(Color.YELLOW);
                time = 0;
                ct.cancel();
                ct.start();


                submit_button.setText(R.string.next_button_text);
                submit_button.setTag("next");
                submit_button.setEnabled(true);

                guessLetter.setEnabled(false);


            }

        }
        if (numberOfAttemptsUsed == attemptLimit) {
            String crossMark = "\u2716";
            messagetoast = Toast.makeText(this,
                    "Wrong! " + crossMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.RED);
            messagetoast.show();

            correct_text = findViewById(R.id.hint_correct_name_text);
            displayText = "Correct Answer: " + carMakeTofind.toUpperCase();// contains the correct answer
            correct_text.setText(displayText);
            correct_text.setBackgroundColor(Color.YELLOW);
            time = 0;
            ct.cancel();
            ct.start();


            submit_button.setText(R.string.next_button_text);
            submit_button.setTag("next");
            submit_button.setEnabled(true);

            guessLetter.setEnabled(false);


        }


    }


}




