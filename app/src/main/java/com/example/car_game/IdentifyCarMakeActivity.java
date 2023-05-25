package com.example.car_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class IdentifyCarMakeActivity extends AppCompatActivity {
    public static final Random random_image=new Random();
    // an array of image names
    private String[] carImages = {"audi_1", "audi_2", "audi_3", "audi_4", "audi_5", "mercedes_1", "mercedes_2", "mercedes_3", "mercedes_4", "mercedes_5", "bmw_1", "bmw_2", "bmw_3", "bmw_4", "bmw_5", "ford_1", "ford_2", "ford_3", "ford_4", "ford_5", "honda_1", "honda_2", "honda_3", "honda_4", "toyota_1", "toyota_2", "toyota_3", "toyota_4", "toyota_5","toyota_6"};

    private String stringImageName;// image name in string
    private int time;//default time value
    private String switchMode="";
    public static final String EXTRA_MESSAGE1 = "com.example.car_game.MESSAGE";
    private TextView timerText;//contains the timer
    private ImageView random_imageView;//contains the random image
    private Button identify_button;
    private String imageName;//the image name in string
    private Spinner spinner;//spinner
    private String selectedOption;//selected spinner option
    private TextView correct_text;
    private String correctText;
    private Toast messageToast;
    private CountDownTimer ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_make);

        time=20;
        random_imageView = findViewById(R.id.random_image_view);
        Intent intent=getIntent();
        switchMode=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        identifyCarMakeStart();//starts the game

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    public void gameOneInitialize(){
        timerText= findViewById(R.id.hint_timer);
        random_imageView = findViewById(R.id.random_image_view);
        Resources res = getResources();
        int resID = res.getIdentifier(stringImageName, "drawable", getPackageName());
        random_imageView.setImageResource(resID);
        if (switchMode.equals("on")){
            timerText.setText("Timer: " + checkDigit(time)+" s");
        }
    }

    //this method prevents the activity from restarting when the orientation is changed and display current images,time etc
    //code referred at https://stackoverflow.com/questions/456211/activity-restart-on-rotation-android
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_identify_car_make);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gameOneInitialize();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            gameOneInitialize();
        }

    }


    //this method modifies the default back in the android app in order stop to the timer manually
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            time=0;
            //stops the timer when the default button is pressed
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    //start method
    public void identifyCarMakeStart(){
        //checks if the user has enabled the timer
        if (switchMode.equals("on")){
            timerCarMake();
        }
        //takes the string element from carImages array and is converted into drawable resource id to put images in the imageview

        stringImageName=nextImage();
        Resources res = getResources();
        int resID = res.getIdentifier(stringImageName, "drawable", getPackageName());
        random_imageView.setImageResource(resID);


    }

   
    //method used when a user clicks on identify button
    public void identifyImage(View view) {
        identify_button = findViewById(R.id.identify_button);
        imageName = stringImageName.substring(0, stringImageName.indexOf('_')).toLowerCase();
        spinner = findViewById(R.id.car_make_spinner);
        selectedOption = spinner.getSelectedItem().toString().toLowerCase();

        if (identify_button.getTag().equals("next")) {
            Intent nextCarMakeIntent = new Intent(IdentifyCarMakeActivity.this, IdentifyCarMakeActivity.class);
            nextCarMakeIntent.putExtra(EXTRA_MESSAGE1,switchMode);
            finish();
            startActivity(nextCarMakeIntent);

        } else {//runs the activity for the first time
            // if "Select car make" has been selected.
            if (selectedOption.equals("select car make")) {
                messageToast = Toast.makeText(this,
                        "Please Select a Car Make",
                        Toast.LENGTH_SHORT);
                messageToast.show();


                // if the correct option has been selected
            } else if (selectedOption.equals(imageName)) {
                String checkedMark = "\u2713";
                messageToast = Toast.makeText(this,
                        "Correct! "+ checkedMark,
                        Toast.LENGTH_SHORT);
                messageToast.getView().setBackgroundColor(Color.GREEN);
                messageToast.show();
                time=0;


                //identify image button changes
                identify_button.setText(R.string.next_button_text);
                identify_button.setTag("next");
                identify_button.setEnabled(true);




                // if the correct option has been selected
            } else {
                String crossMark = "\u2716";
                messageToast = Toast.makeText(this,
                        "Wrong! "+crossMark,
                        Toast.LENGTH_SHORT);
                messageToast.getView().setBackgroundColor(Color.RED);
                messageToast.show();


                correct_text = findViewById(R.id.correct_car_make_text);
                correctText = "Correct Car Make: " + imageName.toUpperCase();// contains the correct answer
                correct_text.setText(correctText);
                correct_text.setBackgroundColor(Color.YELLOW);

                time=0;


                //identify image button changes
                identify_button.setText(R.string.next_button_text);
                identify_button.setTag("next");
                identify_button.setEnabled(true);

            }

        }

    }


    //returns a random image
    private String nextImage(){
        return carImages[random_image.nextInt(carImages.length)];

    }
    //converts the int data in to double digit
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    //method that contains the timer
    //code reference at https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android/10032406#10032406
    public void timerCarMake(){

        timerText= findViewById(R.id.hint_timer);
        ct=new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Timer: " + checkDigit(time)+" s");
                if (time!=0){
                    time--;
                }else{
                    timerText.setText("Timer: " + checkDigit(time)+" s");
                    cancel();
                }

            }
            public void onFinish() {
                timerText.setText("Timer: " + checkDigit(time)+" s");
                timerCondition();
            }
        }.start();
    }

    //method used when the time reaches 0
    public void timerCondition(){
        identify_button = findViewById(R.id.identify_button);
        imageName = stringImageName.substring(0, stringImageName.indexOf('_')).toLowerCase();
        spinner = findViewById(R.id.car_make_spinner);
        selectedOption = spinner.getSelectedItem().toString().toLowerCase();



            // if the correct option has been selected
        if (selectedOption.equals(imageName)) {
            String checkedMark = "\u2713";
            messageToast = Toast.makeText(this,
                    "Correct! "+ checkedMark,
                    Toast.LENGTH_SHORT);
            messageToast.getView().setBackgroundColor(Color.GREEN);
            messageToast.show();

            time=0;
            timerText= findViewById(R.id.hint_timer);
            timerText.setText("Timer: "+time );
            //identify image button changes
            identify_button.setText(R.string.next_button_text);
            identify_button.setTag("next");
            identify_button.setEnabled(true);
            timerCarMake();


            // if the correct option has been selected
        } else {
            String crossMark = "\u2716";
            messageToast = Toast.makeText(this,
                    "Wrong! "+crossMark,
                    Toast.LENGTH_SHORT);
            messageToast.getView().setBackgroundColor(Color.RED);
            messageToast.show();

            time=0;
            timerText= findViewById(R.id.hint_timer);
            timerText.setText("Timer: "+time );
            correct_text = findViewById(R.id.correct_car_make_text);
            correctText = "Correct Car Make: " + imageName.toUpperCase();// contains the correct answer
            correct_text.setText(correctText);
            correct_text.setBackgroundColor(Color.YELLOW);
            timerCarMake();
            //identify button changes
            identify_button.setText(R.string.next_button_text);
            identify_button.setTag("next");
            identify_button.setEnabled(true);

        }


    }

}