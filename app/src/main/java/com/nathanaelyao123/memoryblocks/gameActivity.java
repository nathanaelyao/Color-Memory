package com.nathanaelyao123.memoryblocks;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class gameActivity extends AppCompatActivity implements View.OnClickListener {

    String[] colors = {"yellow", "red", "green", "blue"};
    int round = 0;
    int score = 0;
    ArrayList<String> order = new ArrayList<String>();
    ArrayList<String> guess = new ArrayList<String>();
    Boolean lost = false;
    MediaPlayer mediaPlayer;
    MediaPlayer thump;
    private InterstitialAd mInterstitialAd;
    private Boolean isStarted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button redBtn = (Button)findViewById(R.id.redBtn);
        Button blueBtn = (Button)findViewById(R.id.blueBtn);
        Button greenBtn = (Button)findViewById(R.id.greenBtn);
        Button yellowBtn = (Button)findViewById(R.id.yellowBtn);
        final Button nextRdBtn = (Button)findViewById(R.id.nextRdBtn);
        final ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar);
        progress.setMax(round+2);
        progress.setProgress(2);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3972924677187965/5468886973");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.jazz2);
        //mediaPlayer.start();

        Toast toast = Toast.makeText(this, "PRESS START TO BEGIN!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        redBtn.setOnClickListener(this);
        blueBtn.setOnClickListener(this);
        greenBtn.setOnClickListener(this);
        yellowBtn.setOnClickListener(this);

        nextRdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoss();
                if (round > 0 && !lost) {
                    toast();
                }
                resetProgress();
                order.clear();
                guess.clear();
                nextRdBtn.setText("CHECK ANSWER");
                round++;
                progress.setMax(round + 1);
                generateOrder();
                displayColors();
                checkHighScore();
                setLevel();
                score++;
                nextRdBtn.setVisibility(View.GONE);
            }

        });
    }






    @Override
    public void onClick(View v) {
        thump = MediaPlayer.create(getApplicationContext(), R.raw.thump);
        thump.start();

        Button btn = ((Button) v);
        ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar);
        Button redBtn = (Button)findViewById(R.id.redBtn);
        Button blueBtn = (Button)findViewById(R.id.blueBtn);
        Button greenBtn = (Button)findViewById(R.id.greenBtn);
        Button yellowBtn = (Button)findViewById(R.id.yellowBtn);
        final Button nextRdBtn = (Button)findViewById(R.id.nextRdBtn);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        btn.startAnimation(myAnim);
        checkHighScore();

        switch (btn.getId()) {
            case R.id.blueBtn:
                guess.add("blue");
                break;
            case R.id.redBtn:
                guess.add("red");
                break;
            case R.id.greenBtn:
                guess.add("green");
                break;
            case R.id.yellowBtn:
                guess.add("yellow");
                break;
        }

        progress.incrementProgressBy(1);

        if (progress.getMax() == progress.getProgress()){
            redBtn.setEnabled(false);
            greenBtn.setEnabled(false);
            blueBtn.setEnabled(false);
            yellowBtn.setEnabled(false);
            //nextRdBtn.setVisibility(View.VISIBLE);
            checkLoss();
            if (round > 0 && !lost) {
                toast();
            }

            new Handler().postDelayed(new Runnable() {
                ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar);
                @Override
                public void run() {
                    resetProgress();
                    order.clear();
                    guess.clear();
                    round++;
                    progress.setMax(round + 1);
                    generateOrder();
                    displayColors();
                    checkHighScore();
                    setLevel();
                    score++;
                }
            }, 2000 );//time in milisecond
        }
    }


    private void generateOrder(){
        for (int i = 0; i < round+1; i++) {
            Random random = new Random();
            int index = random.nextInt(colors.length);
            order.add(colors[index]);
        }

        for (int i = 0; i < round  ; i++) {
            if (order.get(i).equals(order.get(i + 1))) {
                if (!order.get(i).equals("green"))
                    order.set(i, "green");
                else {
                    order.set(i, "red");
                }
            }
        }
    }

    private void displayColors() {
        Button redBtn = (Button)findViewById(R.id.redBtn);
        Button blueBtn = (Button)findViewById(R.id.blueBtn);
        Button greenBtn = (Button)findViewById(R.id.greenBtn);
        Button yellowBtn = (Button)findViewById(R.id.yellowBtn);

        redBtn.setEnabled(false);
        greenBtn.setEnabled(false);
        blueBtn.setEnabled(false);
        yellowBtn.setEnabled(false);

        for (int i = 0; i<order.size();i++){

            if (order.get(i).equals("red")){
                showColor("red",i);
            }
            else if (order.get(i).equals("blue")){
                showColor("blue",i);
            }
            else if (order.get(i).equals("green")){
                showColor("green",i);
            }
            else if (order.get(i).equals("yellow")){
                showColor("yellow",i);
            }
        }
    }

    private void showColor(String s, int i){

        Handler handler = new Handler();
        handler.postDelayed(new MyRunnable(s,i), (i+1)*1000);
    }
    public class MyRunnable implements Runnable{
        private String s;
        private int i;

        public MyRunnable(String s, int i){
            this.s = s;
            this.i = i;
        }

        @Override
        public void run() {
            Button redBtn = (Button)findViewById(R.id.redBtn);
            Button blueBtn = (Button)findViewById(R.id.blueBtn);
            Button greenBtn = (Button)findViewById(R.id.greenBtn);
            Button yellowBtn = (Button)findViewById(R.id.yellowBtn);

            if (s.equals("red")){
                animate(redBtn);
            }
            else if(s.equals("blue")){
                animate(blueBtn);
            }
            else if (s.equals("green")){
                animate(greenBtn);
            }
            else if (s.equals("yellow")){
                animate(yellowBtn);
            }
            if (i == order.size()-1){
                redBtn.setEnabled(true);
                greenBtn.setEnabled(true);
                blueBtn.setEnabled(true);
                yellowBtn.setEnabled(true);
            }
        }
    }
    private void animate(Button button){
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        button.startAnimation(myAnim);
    }

    private void resetProgress(){
        ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar);
        progress.setProgress(0);
    }

    private void checkLoss(){
        if (round == 0){
            return;
        }
        for (int i = 0; i<order.size();i++) {
            if (!(guess.get(i).equals(order.get(i)))) {
                lost = true;
                //mediaPlayer.stop();
                Intent intent = new Intent(getApplicationContext(), gameOverActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                finish();
                break;
            }
        }
    }

    private void checkHighScore(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        int highScore = prefs.getInt("key", 0);
        if (score > highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("key", score);
            editor.commit();
        }
        TextView levelText = (TextView)findViewById(R.id.scoreText);
        levelText.setText("High Score: " + highScore);
    }

    private void setLevel(){
        TextView levelText = (TextView)findViewById(R.id.levelText);
        levelText.setText("Level: " + round);
    }

    private void toast(){
        Toast toast = Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
