package com.nathanaelyao123.memoryblocks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.*;


public class gameOverActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        if(getIntent().hasExtra("score")){
            int score = getIntent().getIntExtra("score",0);
            TextView levelText = (TextView)findViewById(R.id.finalScoreText);
            levelText.setText("Score: " + score);
        }

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        int hiScore = prefs.getInt("key", 0);

        TextView highScoreText = (TextView)findViewById(R.id.highScoreText);
        highScoreText.setText("High Score: " + hiScore );

        Button restartBtn = (Button)findViewById(R.id.restartBtn);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), gameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*Button leaderboardBtn = (Button)findViewById(R.id.leaderboardBtn);
        leaderboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInClient signInClient = GoogleSignIn.getClient(this, DEFAULT_GAMES_SIGN_IN);
                Intent intent = signInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);

            }
        });*/
    }

}
