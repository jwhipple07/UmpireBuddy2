package com.example.jw043373.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtStrikeCount;
    TextView txtBallCount;
    Button strikeButton;
    Button ballButton;
    int strikeCountNum=0;
    int ballCountNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INFO :: code for the striek counter
        txtStrikeCount = (TextView) findViewById(R.id.strike_counter_text);
        txtStrikeCount.setText(String.valueOf(strikeCountNum));
        strikeButton = (Button)findViewById(R.id.strike_button);

        strikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strikeCountNum++;   //INFO :: increase the strike count
                txtStrikeCount.setText(String.valueOf(strikeCountNum));
                if(strikeCountNum == 3){
                    actionAlert("Out!!");   //INFO :: if it was strike 3, then alert and reset
                }
            }
        });

        //INFO :: code for the ball counter
        txtBallCount = (TextView) findViewById(R.id.ball_counter_text);
        txtBallCount.setText(String.valueOf(ballCountNum));
        ballButton = (Button)findViewById(R.id.ball_button);

        ballButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ballCountNum++; //INFO :: increase the ball count
                txtBallCount.setText(String.valueOf(ballCountNum));
                if(ballCountNum == 4){
                    actionAlert("Walk!!");  //INFO :: if it was ball 4, then alert and reset
                }
            }
        });

    }

    /**
     * Reset the counters to default values.
     */
    protected void clearCount(){
        strikeCountNum = 0;
        ballCountNum = 0;
        txtStrikeCount = (TextView) findViewById(R.id.strike_counter_text);
        txtStrikeCount.setText(String.valueOf(strikeCountNum));

        txtBallCount = (TextView) findViewById(R.id.ball_counter_text);
        txtBallCount.setText(String.valueOf(ballCountNum));
    }

    /**
     * Alert the user that a max has been reached, on affirmative the count will be reset.
     * @param call - The message to display
     */
    protected void actionAlert(String call){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(call)
                .setCancelable(false) //INFO :: user has to accept
                .setPositiveButton("Clear Count", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearCount();   //INFO :: reset the ball/strike counter
                    }
                }).create().show();
    }
}
