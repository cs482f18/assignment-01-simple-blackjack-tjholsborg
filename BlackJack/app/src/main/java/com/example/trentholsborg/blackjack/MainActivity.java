package com.example.trentholsborg.blackjack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /**
     * Creates the view of the Main Menu
     * @param savedInstanceState the state of thee View
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called by the button click of New Game on the start menu. Opens the second activity, the Game.
     * @param v
     */
    public void startGame(View v)
    {
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
}
