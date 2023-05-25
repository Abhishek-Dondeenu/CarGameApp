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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class IdentifyCarImageActivity extends AppCompatActivity {

    private String[] carImages1 = {"audi_1", "audi_2", "audi_3", "audi_4", "audi_5","mercedes_1", "mercedes_2", "mercedes_3", "mercedes_4", "mercedes_5"};// an array of image names to be used for the first imageView
    private String[] carImages2 = {"bmw_1", "bmw_2", "bmw_3", "bmw_4", "bmw_5", "ford_1", "ford_2", "ford_3", "ford_4", "ford_5"};// an array of image names to be used for the second imageView
    private String[] carImages3 = {"honda_1", "honda_2", "honda_3", "honda_4", "toyota_1", "toyota_2", "toyota_3", "toyota_4", "toyota_5","toyota_6"};// an array of image names to be used third imageView
    public static final Random random=new Random();
    private String imageName1;//the name of the image to be used to display in the image view
    private String imageName2;//the name of the image to be used to display in the image view
    private String imageName3;//the name of the image to be used to display in the image view
    private String carImageName1;//contains the brand name of the first car image
    private String carImageName2;//contains the brand name of the second car image
    private String carImageName3;//contains the brand name of the third car image
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private Button nextBtn;
    private ArrayList<String> randomImageNames = new ArrayList<>();//contains the 3 names of the 3 car images
    private String imageToFind;// name of a car manufacturer corresponding to one of the displayed images
    private int time;//default time value
    private String switchMode="";
    public static final String EXTRA_MESSAGE3 = "com.example.car_game.MESSAGE";
    private ArrayList<String> used = new ArrayList<>();
    private TextView timerText;//contains the timer
    private TextView carMakeNameText;
    private CountDownTimer ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_image);

        imageView1 = findViewById(R.id.first_image_view);
        imageView2 = findViewById(R.id.second_image_view);
        imageView3 = findViewById(R.id.third_image_view);



        time=20;
        Intent intent=getIntent();
        switchMode=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);//typecasted
        identifyCarImageStart();

    }
    private void gameThreeInitialize(){
        timerText= findViewById(R.id.identify_car_image_timer);
        imageView1 = findViewById(R.id.first_image_view);
        imageView2 = findViewById(R.id.second_image_view);
        imageView3 = findViewById(R.id.third_image_view);
        carMakeNameText=findViewById(R.id.car_manufacturer_name);

        if (switchMode.equals("on")){
            timerText.setText("Timer: " + checkDigit(time)+" s");
        }

        Resources res = getResources();
        int resID = res.getIdentifier(imageName1, "drawable", getPackageName());
        imageView1.setImageResource(resID);
        Resources res2 = getResources();
        int resID2 = res2.getIdentifier(imageName2, "drawable", getPackageName());
        imageView2.setImageResource(resID2);
        Resources res3 = getResources();
        int resID3 = res3.getIdentifier(imageName3, "drawable", getPackageName());
        imageView3.setImageResource(resID3);

        carMakeNameText.setText("Car Manufacturer Name: "+imageToFind.toUpperCase());
        nextBtn=findViewById(R.id.image_next_btn);
        nextBtn.setEnabled(false);



    }
    //this method prevents the activity from restarting when the orientation is changed and displays current images,time etc
    //code referred at https://stackoverflow.com/questions/456211/activity-restart-on-rotation-android
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_identify_car_image);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gameThreeInitialize();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
           gameThreeInitialize();
        }
    }
    //this method modifies the default back in the android app in order stop to the timer manually
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            time=0;//stops the timer when the default button is pressed
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    //start method
    public void identifyCarImageStart(){
        nextBtn=findViewById(R.id.image_next_btn);
        nextBtn.setEnabled(false);
        randomImageNames.clear();//clears the previous car make names

        //setting an image for the first imageView
        imageName1=nextImageOne();//gets a random image name
        Resources res = getResources();
        int resID = res.getIdentifier(imageName1, "drawable", getPackageName());
        imageView1.setImageResource(resID);
        carImageName1=imageName1.substring(0, imageName1.indexOf('_'));
        randomImageNames.add(carImageName1);
        imageView1.setContentDescription(carImageName1);

        //setting an image for the second imageView
        imageName2=nextImageTwo();//gets a random image name
        Resources res2 = getResources();
        int resID2 = res2.getIdentifier(imageName2, "drawable", getPackageName());
        imageView2.setImageResource(resID2);
        carImageName2=imageName2.substring(0, imageName2.indexOf('_'));
        randomImageNames.add(carImageName2);
        imageView2.setContentDescription(carImageName2);

        //setting an image for the third imageView
        imageName3=nextImageThree();//gets a random image name
        Resources res3 = getResources();
        int resID3 = res3.getIdentifier(imageName3, "drawable", getPackageName());
        imageView3.setImageResource(resID3);
        carImageName3=imageName3.substring(0, imageName3.indexOf('_'));
        randomImageNames.add(carImageName3);
        imageView3.setContentDescription(carImageName3);

        //Gets a random image name from the 3 car images used
        int randomIndex = (int) (Math.random() * randomImageNames.size());
        imageToFind = randomImageNames.get(randomIndex);
        carMakeNameText=findViewById(R.id.car_manufacturer_name);
        carMakeNameText.setText("Car Manufacturer Name: "+imageToFind.toUpperCase());

        //checks if the user has enabled the timer
        if (switchMode.equals("on")){
            timerCarImage();
        }

    }

    //The first image compare method
    public void firstImageCompare(View view) {
        imageView1 = findViewById(R.id.first_image_view);
        imageView2 = findViewById(R.id.second_image_view);
        imageView3 = findViewById(R.id.third_image_view);
        nextBtn=findViewById(R.id.image_next_btn);

        //compares the description which has the car make name to the given car make mentioned
        if(imageView1.getContentDescription().toString().equals(imageToFind)){
            //gives "CORRECT!" toast message
            String checkedMark = "\u2713";
            Toast messagetoast = Toast.makeText(this,
                    "Correct! "+ checkedMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.GREEN);
            messagetoast.show();

            TextView guideText = findViewById(R.id.text_guide);
            String displayText = "Click the 'Next' button to play again ";
            guideText.setText(displayText);

            //disables the image click for the three images after user clicks an image
            imageView1.setEnabled(false);
            imageView2.setEnabled(false);
            imageView3.setEnabled(false);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundColor(Color.parseColor("#FFD700"));
            time=0;


        }else{
            String crossMark = "\u2716";
            Toast messagetoast = Toast.makeText(this,
                    "Wrong! "+crossMark,
                    Toast.LENGTH_SHORT);

            messagetoast.getView().setBackgroundColor(Color.RED);
            messagetoast.show();
            TextView guideText = findViewById(R.id.text_guide);
            String displayText = "Click the 'Next' button to play again ";
            guideText.setText(displayText);

            imageView1.setEnabled(false);
            imageView2.setEnabled(false);
            imageView3.setEnabled(false);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundColor(Color.parseColor("#FFD700"));
            time=0;

        }

    }

    //The second image compare method
    public void secondImageCompare(View view) {
        imageView1 = findViewById(R.id.first_image_view);
        imageView2 = findViewById(R.id.second_image_view);
        imageView3 = findViewById(R.id.third_image_view);
        nextBtn=findViewById(R.id.image_next_btn);
        if(imageView2.getContentDescription().toString().equals(imageToFind)){
            String checkedMark = "\u2713";
            Toast messagetoast = Toast.makeText(this,
                    "Correct! "+ checkedMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.GREEN);
            messagetoast.show();

            TextView guideText = findViewById(R.id.text_guide);
            String displayText = "Click the 'Next' button to play again ";
            guideText.setText(displayText);

            imageView1.setEnabled(false);
            imageView2.setEnabled(false);
            imageView3.setEnabled(false);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundColor(Color.parseColor("#FFD700"));
            time=0;


        }else{
            String crossMark = "\u2716";
            Toast messagetoast = Toast.makeText(this,
                    "Wrong! "+crossMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.RED);
            messagetoast.show();

            TextView guideText = findViewById(R.id.text_guide);
            String displayText = "Click the 'Next' button to play again ";
            guideText.setText(displayText);

            imageView1.setEnabled(false);
            imageView2.setEnabled(false);
            imageView3.setEnabled(false);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundColor(Color.parseColor("#FFD700"));
            time=0;

        }

    }

    //The third image compare method
    public void thirdImageCompare(View view) {
        imageView1 = findViewById(R.id.first_image_view);
        imageView2 = findViewById(R.id.second_image_view);
        imageView3 = findViewById(R.id.third_image_view);
        nextBtn=findViewById(R.id.image_next_btn);
        if(imageView3.getContentDescription().toString().equals(imageToFind)){
            String checkedMark = "\u2713";
            Toast messagetoast = Toast.makeText(this,
                    "Correct! "+ checkedMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.GREEN);
            messagetoast.show();

            TextView guideText = findViewById(R.id.text_guide);
            String displayText = "Click the 'Next' button to play again ";
            guideText.setText(displayText);

            imageView1.setEnabled(false);
            imageView2.setEnabled(false);
            imageView3.setEnabled(false);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundColor(Color.parseColor("#FFD700"));
            time=0;

        }else{
            String crossMark = "\u2716";
            Toast messagetoast = Toast.makeText(this,
                    "Wrong! "+crossMark,
                    Toast.LENGTH_SHORT);
            messagetoast.getView().setBackgroundColor(Color.RED);
            messagetoast.show();

            TextView guideText = findViewById(R.id.text_guide);
            String displayText = "Click the 'Next' button to play again ";
            guideText.setText(displayText);

            imageView1.setEnabled(false);
            imageView2.setEnabled(false);
            imageView3.setEnabled(false);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundColor(Color.parseColor("#FFD700"));
            time=0;

        }

    }

    //starts a new game for the user
    public void newGame(View view) {
        Intent nextIdentifyintent = new Intent(IdentifyCarImageActivity.this, IdentifyCarImageActivity.class);
        nextIdentifyintent.putExtra(EXTRA_MESSAGE3,switchMode);
        finish();
        startActivity(nextIdentifyintent);
    }

    //returns an random image name for the first image
    private String nextImageOne(){
        return carImages1[random.nextInt(carImages1.length)];

    }
    //returns an random image name for the second image
    private String nextImageTwo(){
        return carImages2[random.nextInt(carImages2.length)];

    }
    //returns an random image name for the third image
    private String nextImageThree(){
        return carImages3[random.nextInt(carImages3.length)];

    }

    //timer method
    //code reference at https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android/10032406#10032406
    public void timerCarImage(){
        timerText= findViewById(R.id.identify_car_image_timer);
        ct=new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerText.setText("Timer: " + checkDigit(time)+" s");
                if (time!=0){
                    time--;
                }else{
                    cancel();
                }
            }
            public void onFinish() {
                timerText.setText("Timer: " + checkDigit(time)+" s");
                timerCondition();

            }
        }.start();

    }
    //method used when timer reaches 0
    public void timerCondition(){
        Toast messagetoast = Toast.makeText(this,
                "Time out! ",
                Toast.LENGTH_SHORT);

        messagetoast.show();
        messagetoast.setGravity(Gravity.TOP, 0, 0);
        Intent nextIdentifyintent = new Intent(IdentifyCarImageActivity.this, IdentifyCarImageActivity.class);
        nextIdentifyintent.putExtra(EXTRA_MESSAGE3,switchMode);
        finish();
        startActivity(nextIdentifyintent);
    }
    //converts the int data in to double digit
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


}