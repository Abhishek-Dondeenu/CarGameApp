package com.example.car_game;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.car_game.MESSAGE";
    private String timerMessage;
    private Switch timerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerSwitch=findViewById(R.id.timerSwitch);

    }


    public void identifyCarMake(View view) {

        if(timerSwitch.isChecked()){
            timerMessage = "on";
        }else{
            timerMessage = "off";
        }
        Intent gameOne = new Intent(this, IdentifyCarMakeActivity.class);
        gameOne.putExtra(EXTRA_MESSAGE,timerMessage);
        startActivity(gameOne);


    }


    public void hintGame(View view) {
        if(timerSwitch.isChecked()){
            timerMessage = "on";
        }else{
            timerMessage = "off";
        }

        Intent gameTwo = new Intent(this, HintActivity.class);
        gameTwo.putExtra(EXTRA_MESSAGE,timerMessage);
        startActivity(gameTwo);
    }

    public void identifyCarImage(View view) {
        if(timerSwitch.isChecked()){
            timerMessage = "on";
        }else{
            timerMessage = "off";
        }
        Intent gameThree = new Intent(this, IdentifyCarImageActivity.class);
        gameThree.putExtra(EXTRA_MESSAGE,timerMessage);
        startActivity(gameThree);
    }


    public void advancedLevel(View view) {
        if(timerSwitch.isChecked()){
            timerMessage = "on";
        }else{
            timerMessage = "off";
        }

        Intent gameFour = new Intent(this, AdvancedLevelActivity.class);
        gameFour.putExtra(EXTRA_MESSAGE,timerMessage);
        startActivity(gameFour);

    }




}