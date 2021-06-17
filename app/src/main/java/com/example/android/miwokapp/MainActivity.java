package com.example.android.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Setting up the onClickListeners for the different TextViews available
         * on the MainScreen(activity
         */
        //Find the view that shows Numbers category on Miwok app
        TextView numbers = (TextView) findViewById(R.id.numbers);
        //set the onClickListener for the Numbers Activity
        numbers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Creating new intent {@ Link NumberActivity}
                Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);
                //starting the Intent
                startActivity(numbersIntent);
            }
        });

        //Find the view that shows Family category on Miwok App
        TextView family = (TextView) findViewById(R.id.family);
        //set the onClickListener for the Family Activity
        family.setOnClickListener(new View.OnClickListener() {;



            @Override
            public void onClick(View view) {

                //Creating new intent {@ Link FamilyActivity}
                Intent familyIntent = new Intent(MainActivity.this, FamilyActivity.class);
                //starting the Intent
                startActivity(familyIntent);
            }


        });

        //Find the view that shows Colors category on Miwok App
        TextView color = (TextView) findViewById(R.id.colors);
        //set the onClickListener for the Colors Activity
        color.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Creating new intent {@ Link ColorsActivity}
                Intent colorIntent = new Intent(MainActivity.this, ColorsActivity.class);
                //starting the Intent
                startActivity(colorIntent);
            }


        });
        //Find the TextView that shows Family Activity on Miwok App
        TextView phrases = (TextView) findViewById(R.id.phrases);
        //set the onClickListener for the Phrases Activity
        phrases.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Creating new intent {@ Link PhrasesActivity}
                Intent phraseIntent = new Intent(MainActivity.this, PhrasesActivity.class);
                //starting the Intent
                startActivity(phraseIntent);


            }


        });


    }
}
