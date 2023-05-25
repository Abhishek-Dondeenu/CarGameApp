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

import java.util.Random;

public class AdvancedLevelActivity extends AppCompatActivity {
    public static final Random random=new Random();

    private String[] carImages1 = {"audi_1", "audi_2", "audi_3", "audi_4", "audi_5","mercedes_1", "mercedes_2", "mercedes_3", "mercedes_4", "mercedes_5"};// an array of image names to be used for the first imageView
    private String[] carImages2 = {"bmw_1", "bmw_2", "bmw_3", "bmw_4", "bmw_5", "ford_1", "ford_2", "ford_3", "ford_4", "ford_5"};// an array of image names to be used for the second imageView
    private String[] carImages3 = {"honda_1", "honda_2", "honda_3", "honda_4", "toyota_1", "toyota_2", "toyota_3", "toyota_4", "toyota_5","toyota_6"};// an array of image names third imageView

    private String imageName1;//the name of the image to be used to display in the first image view
    private String imageName2;//the name of the image to be used to display in the second image view
    private String imageName3;//the name of the image to be used to display in the third image view

    private String carImageName1;//contains the string value of the first car image
    private String carImageName2;//contains the string value of the second car image
    private String carImageName3;//contains the string value of the third car image

    private int score;//score
    private int time;//time in seconds
    private int numberOfAttemptsUsed;//number of attempts used
    public static final int attemptLimit=3;
    private String switchMode="";
    public static final String EXTRA_MESSAGE4 = "com.example.car_game.MESSAGE";

    private ImageView imageView1;//ImageView for the first image
    private ImageView imageView2;//ImageView for the second image
    private ImageView imageView3;//ImageView for the third image

    private Button submitButton;//submit button

    private EditText textBoxOne;//text box for the first image
    private EditText textBoxTwo;//text box for the second image
    private EditText textBoxThree;//text box for the third image

    private String firstAnswer;//given answer for the first image
    private String secondAnswer;//given answer for the second image
    private String thirdAnswer;//given answer for the third image

    private TextView correctAnswerOne;//correct answer for the first image
    private TextView correctAnswerTwo;//correct answer for the second image
    private TextView correctAnswerThree;//correct answer for the third image

    private TextView scoreBox;//contains the score
    private TextView timerText;//contains the timer
    private CountDownTimer ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        imageView1 = findViewById(R.id.firstImageView);
        imageView2 = findViewById(R.id.secondImageView);
        imageView3 = findViewById(R.id.thirdImageView);

        Intent intent=getIntent();
        switchMode=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);//typecasted

        //code reference at https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android/10032406#10032406
        timerText= findViewById(R.id.advanced_timer);
        ct=new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerText.setText("Timer: " + checkDigit(time)+" s");
                if (time!=0){
                    time--;
                }else{
                    this.cancel();
                }

            }
            public void onFinish() {
                timerText.setText("Timer: " + checkDigit(time)+" s");
                timerCondition();

            }
        };

        advancedLevelStart();//starts the game
    }

    public void gameFourInitialize(){
        timerText= findViewById(R.id.advanced_timer);
        imageView1 = findViewById(R.id.firstImageView);
        imageView2 = findViewById(R.id.secondImageView);
        imageView3 = findViewById(R.id.thirdImageView);

        Resources res = getResources();
        int resID = res.getIdentifier(imageName1, "drawable", getPackageName());
        imageView1.setImageResource(resID);
        Resources res2 = getResources();
        int resID2 = res2.getIdentifier(imageName2, "drawable", getPackageName());
        imageView2.setImageResource(resID2);
        Resources res3 = getResources();
        int resID3 = res3.getIdentifier(imageName3, "drawable", getPackageName());
        imageView3.setImageResource(resID3);

        if (switchMode.equals("on")){
            timerText.setText("Timer: " + checkDigit(time)+" s");
        }
    }

    //this method prevents the activity from restarting when the orientation is changed and display current images,time etc
    //code referred at https://stackoverflow.com/questions/456211/activity-restart-on-rotation-android
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_advanced_level);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gameFourInitialize();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            gameFourInitialize();
        }
    }
    //this method modifies the default back in the android app in order stop to the timer manually
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            time=0;//stops the timer when the default button is pressed
            if (switchMode.equals("on")) {
                ct.cancel();
            }
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    //start method for the advanced level
    public void advancedLevelStart(){
        score=0;
        time=20;
        numberOfAttemptsUsed=0;
        //checks if the user has enabled the timer
        if (switchMode.equals("on")){
            ct.start();
        }

        //setting an image for the first imageView
        imageName1=nextImage1();//gets a random image name
        Resources res = getResources();
        int resID = res.getIdentifier(imageName1, "drawable", getPackageName());
        imageView1.setImageResource(resID);
        carImageName1=imageName1.substring(0, imageName1.indexOf('_'));
        imageView1.setContentDescription(carImageName1);


        //setting an image for the second imageView
        imageName2=nextImage2();//gets a random image name
        Resources res2 = getResources();
        int resID2 = res2.getIdentifier(imageName2, "drawable", getPackageName());
        imageView2.setImageResource(resID2);
        carImageName2=imageName2.substring(0, imageName2.indexOf('_'));
        imageView2.setContentDescription(carImageName2);


        //setting an image for the third imageView
        imageName3=nextImage3();//gets a random image name
        Resources res3 = getResources();
        int resID3 = res3.getIdentifier(imageName3, "drawable", getPackageName());
        imageView3.setImageResource(resID3);
        carImageName3=imageName3.substring(0, imageName3.indexOf('_'));
        imageView3.setContentDescription(carImageName3);

    }

    //submit method for the advanced level
    public void submitAnswer(View view) {
        time=20;
        submitButton = findViewById(R.id.advanced_submit_btn);

        textBoxOne= findViewById(R.id.textBoxOne);
        textBoxTwo= findViewById(R.id.textBoxTwo);
        textBoxThree= findViewById(R.id.textBoxThree);

        firstAnswer=textBoxOne.getText().toString().toLowerCase();
        secondAnswer=textBoxTwo.getText().toString().toLowerCase();
        thirdAnswer=textBoxThree.getText().toString().toLowerCase();

        imageView1 = findViewById(R.id.firstImageView);
        imageView2 = findViewById(R.id.secondImageView);
        imageView3 = findViewById(R.id.thirdImageView);

        correctAnswerOne= findViewById(R.id.correctAnswerOne);
        correctAnswerTwo= findViewById(R.id.correctAnswerTwo);
        correctAnswerThree= findViewById(R.id.correctAnswerThree);

        scoreBox= findViewById(R.id.scoreBox);

        if(submitButton.getTag().equals("next")){
            //leads the user to a new game
            Intent nextAdvancedIntent = new Intent(AdvancedLevelActivity.this, AdvancedLevelActivity.class);
            nextAdvancedIntent.putExtra(EXTRA_MESSAGE4,switchMode);
            finish();
            startActivity(nextAdvancedIntent);
        }else {
            //checks whether the first answer is right or wrong
            if (firstAnswer.equals(imageView1.getContentDescription())){
                textBoxOne.setBackgroundResource(R.drawable.green_background_color);
                textBoxOne.setEnabled(false);
            }else {
                textBoxOne.setBackgroundResource(R.drawable.red_background_color);
                textBoxOne.setText("");
            }

            //checks whether the second answer is right or wrong
            if (secondAnswer.equals(imageView2.getContentDescription())){
                textBoxTwo.setBackgroundResource(R.drawable.green_background_color);
                textBoxTwo.setEnabled(false);
            }else {
                textBoxTwo.setBackgroundResource(R.drawable.red_background_color);
                textBoxTwo.setText("");


            }

            //checks whether the third answer is right or wrong
            if (thirdAnswer.equals(imageView3.getContentDescription())){
                textBoxThree.setBackgroundResource(R.drawable.green_background_color);
                textBoxThree.setEnabled(false);
            }else {
                textBoxThree.setBackgroundResource(R.drawable.red_background_color);
                textBoxThree.setText("");


            }

            //checks whether all of the answers are correct
            if (firstAnswer.equals(imageView1.getContentDescription())&&secondAnswer.equals(imageView2.getContentDescription())&&thirdAnswer.equals(imageView3.getContentDescription())){
                String checkedMark = "\u2713";
                Toast messagetoast = Toast.makeText(this,
                        "Correct! "+ checkedMark,
                        Toast.LENGTH_SHORT);
                messagetoast.getView().setBackgroundColor(Color.GREEN);
                messagetoast.show();

                submitButton.setText(R.string.next_button_text);
                submitButton.setTag("next");
                submitButton.setEnabled(true);
                time=0;
                if (switchMode.equals("on")) {
                    ct.cancel();
                    ct.start();
                }


            } else {
                numberOfAttemptsUsed++;//increases the attempts used
                Toast messagetoast = Toast.makeText(this,
                        "Number of attempts left: "+(attemptLimit-numberOfAttemptsUsed),//displays the attempts remaining
                        Toast.LENGTH_SHORT);
                messagetoast.show();
                messagetoast.setGravity(Gravity.TOP, 0, 0);

                time=20;//resets the timer after each attempt
                if (switchMode.equals("on")) {
                    ct.cancel();
                    ct.start();
                }


            }

            if (numberOfAttemptsUsed==attemptLimit) {
                String crossMark = "\u2716";
                Toast messagetoast = Toast.makeText(this,
                        "Wrong! " + crossMark,
                        Toast.LENGTH_SHORT);
                messagetoast.getView().setBackgroundColor(Color.RED);
                messagetoast.show();

                submitButton.setText(R.string.next_button_text);
                submitButton.setTag("next");
                submitButton.setEnabled(true);
                time=0;
                if (switchMode.equals("on")) {
                    ct.cancel();
                    ct.start();
                }


                if (!firstAnswer.equals(imageView1.getContentDescription())){
                    String correctText = "Correct Answer: " + imageView1.getContentDescription().toString().toUpperCase();// contains the correct answer
                    correctAnswerOne.setText(correctText);
                    correctAnswerOne.setBackgroundColor(Color.YELLOW);
                }else {
                    score++;

                }
                if (!secondAnswer.equals(imageView2.getContentDescription())){
                    String correctText = "Correct Answer: " + imageView2.getContentDescription().toString().toUpperCase();// contains the correct answer
                    correctAnswerTwo.setText(correctText);
                    correctAnswerTwo.setBackgroundColor(Color.YELLOW);

                }else{
                    score++;
                }
                if (!thirdAnswer.equals(imageView3.getContentDescription())){
                    String correctText = "Correct Answer: " + imageView3.getContentDescription().toString().toUpperCase();// contains the correct answer
                    correctAnswerThree.setText(correctText);
                    correctAnswerThree.setBackgroundColor(Color.YELLOW);

                }else{
                    score++;
                }
            }

            String finalScore = "Score: " + (score);//contains the final score
            scoreBox.setText(finalScore);//sets the score in the level

        }

    }

    //returns an random image
    private String nextImage1(){
        return carImages1[random.nextInt(carImages1.length)];

    }
    //returns an random image
    private String nextImage2(){
        return carImages2[random.nextInt(carImages2.length)];

    }
    //returns an random image
    private String nextImage3(){
        return carImages3[random.nextInt(carImages3.length)];

    }

    //method used when the timer reaches 0
    public void timerCondition(){
        submitButton = findViewById(R.id.advanced_submit_btn);

        textBoxOne= findViewById(R.id.textBoxOne);
        textBoxTwo= findViewById(R.id.textBoxTwo);
        textBoxThree= findViewById(R.id.textBoxThree);

        firstAnswer=textBoxOne.getText().toString().toLowerCase();
        secondAnswer=textBoxTwo.getText().toString().toLowerCase();
        thirdAnswer=textBoxThree.getText().toString().toLowerCase();

        imageView1 = findViewById(R.id.firstImageView);
        imageView2 = findViewById(R.id.secondImageView);
        imageView3 = findViewById(R.id.thirdImageView);

        correctAnswerOne= findViewById(R.id.correctAnswerOne);
        correctAnswerTwo= findViewById(R.id.correctAnswerTwo);
        correctAnswerThree= findViewById(R.id.correctAnswerThree);

        scoreBox= findViewById(R.id.scoreBox);
        if (firstAnswer.equals(imageView1.getContentDescription())){
            textBoxOne.setBackgroundResource(R.drawable.green_background_color);
            textBoxOne.setEnabled(false);
        }else {
            textBoxOne.setBackgroundResource(R.drawable.red_background_color);
            textBoxOne.setText("");
        }

        //checks whether the second answer is right or wrong
        if (secondAnswer.equals(imageView2.getContentDescription())){
            textBoxTwo.setBackgroundResource(R.drawable.green_background_color);
            textBoxTwo.setEnabled(false);
        }else {
            textBoxTwo.setBackgroundResource(R.drawable.red_background_color);
            textBoxTwo.setText("");


        }

        //checks whether the third answer is right or wrong
        if (thirdAnswer.equals(imageView3.getContentDescription())){
            textBoxThree.setBackgroundResource(R.drawable.green_background_color);
            textBoxThree.setEnabled(false);
        }else {
            textBoxThree.setBackgroundResource(R.drawable.red_background_color);
            textBoxThree.setText("");


        }

        if (firstAnswer.equals(imageView1.getContentDescription())&&secondAnswer.equals(imageView2.getContentDescription())&&thirdAnswer.equals(imageView3.getContentDescription())){
            String checkedMark = "\u2713";
            Toast messagetoast = Toast.makeText(this,
                    "Correct! "+ checkedMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.GREEN);
            messagetoast.show();

            submitButton.setText(R.string.next_button_text);
            submitButton.setTag("next");
            submitButton.setEnabled(true);
            time=0;
            ct.cancel();
            ct.start();
        }

        else {
            numberOfAttemptsUsed++;
            Toast messagetoast = Toast.makeText(this,
                    "Number of attempts left: "+(attemptLimit-numberOfAttemptsUsed) ,
                    Toast.LENGTH_SHORT);
            messagetoast.show();
            messagetoast.setGravity(Gravity.TOP, 0, 0);

            time=20;//resets the timer after each attempt
            ct.cancel();
            ct.start();
        }

        if (numberOfAttemptsUsed==attemptLimit) {
            String crossMark = "\u2716";
            Toast messagetoast = Toast.makeText(this,
                    "Wrong! " + crossMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.RED);
            messagetoast.show();

            submitButton.setText(R.string.next_button_text);
            submitButton.setTag("next");
            submitButton.setEnabled(true);
            time=0;
            ct.cancel();
            ct.start();


            if (!firstAnswer.equals(imageView1.getContentDescription())){
                String correctText = "Correct Answer: " + imageView1.getContentDescription().toString().toUpperCase();// contains the correct answer
                correctAnswerOne.setText(correctText);
                correctAnswerOne.setBackgroundColor(Color.YELLOW);
            }else {
                score++;

            }
            if (!secondAnswer.equals(imageView2.getContentDescription())){
                String correctText = "Correct Answer: " + imageView2.getContentDescription().toString().toUpperCase();// contains the correct answer
                correctAnswerTwo.setText(correctText);
                correctAnswerTwo.setBackgroundColor(Color.YELLOW);

            }else{
                score++;
            }
            if (!thirdAnswer.equals(imageView3.getContentDescription())){
                String correctText = "Correct Answer: " + imageView3.getContentDescription().toString().toUpperCase();// contains the correct answer
                correctAnswerThree.setText(correctText);
                correctAnswerThree.setBackgroundColor(Color.YELLOW);

            }else{
                score++;
            }
        }

        String finalScore = "Score: " + (score);//contains the final score
        scoreBox.setText(finalScore);//sets the score in the level

    }
    //converts the int data in to double digit
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

}