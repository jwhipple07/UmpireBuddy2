package com.example.jw043373.myapplication;

import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActionMode mActionMode;
    private final static String PREFS_NAME = "PrefsFile";
    private final static String OUTS = "Outs";
    private final static String TTS_SELECTION = "TTS";
    private final static String OUTS_COUNT_NAME = "outsCountNum";
    private final static String STRIKES_COUNT_NAME = "strikesCountNum";
    private final static String BALLS_COUNT_NAME = "ballsCountNum";
    private TextView txtStrikeCount;
    private TextView txtBallCount;
    private TextView txtOutsCount;
    private int strikeCountNum;
    private int ballCountNum;
    private int outsCountNum;
    private int linearID;  //this will be used to store the ID of what was long clicked

    private TextToSpeech t1;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        //INFO :: taken from https://github.com/burrise/AndroidSampleCode

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            MenuItem menuItem = menu.findItem(R.id.context_menu_type);
            switch(linearID){
                case R.id.ball_linearLayout:
                    menuItem.setTitle("Balls");
                    break;
                case R.id.strike_linearLayout:
                    menuItem.setTitle("Strikes");
                    break;
                case R.id.out_linearLayout:
                    menuItem.setTitle("Outs");
                    break;
            }
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }
        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.increment_symbol:
                    updateCounter(linearID,true);
                    //mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.decrement_symbol:
                    updateCounter(linearID,false);
                   // mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            outsCountNum = savedInstanceState.getInt(OUTS_COUNT_NAME);
            strikeCountNum = savedInstanceState.getInt(STRIKES_COUNT_NAME);
            ballCountNum = savedInstanceState.getInt(BALLS_COUNT_NAME);
        } else {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            outsCountNum = settings.getInt(OUTS,0);
            strikeCountNum = 0;
            ballCountNum = 0;
        }

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
        //INFO :: code for the strike counter
        txtStrikeCount = (TextView) findViewById(R.id.strike_counter_text);
        txtStrikeCount.setText(String.valueOf(strikeCountNum));

        //INFO :: code for the ball counter
        txtBallCount = (TextView) findViewById(R.id.ball_counter_text);
        txtBallCount.setText(String.valueOf(ballCountNum));
        //INFO :: code for the ball counter
        txtOutsCount = (TextView) findViewById(R.id.outs_counter_text);
        txtOutsCount.setText(String.valueOf(outsCountNum));

        //INFO :: Set long click listeners to edit numbers
        final RelativeLayout ballLayout = (RelativeLayout) findViewById(R.id.ball_linearLayout);
        ballLayout.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
                if (mActionMode != null) {
                    return false;
                }
                linearID = ballLayout.getId();

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });
        final RelativeLayout strikeLayout = (RelativeLayout) findViewById(R.id.strike_linearLayout);
        strikeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
                if (mActionMode != null) {
                    return false;
                }

                linearID = strikeLayout.getId();
                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        final RelativeLayout outsLayout = (RelativeLayout) findViewById(R.id.out_linearLayout);
        outsLayout.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
                if (mActionMode != null) {
                    return false;
                }

                linearID = outsLayout.getId();
                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        View ball_increment = findViewById(R.id.ball_button);
        ball_increment.setOnClickListener(this);
        View strike_increment = findViewById(R.id.strike_button);
        strike_increment.setOnClickListener(this);
    }


    @Override
    public void onClick(View view){
        updateCounter(view.getId(),true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.reset:
                clearCount();
                break;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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

        txtOutsCount = (TextView) findViewById(R.id.outs_counter_text);
        txtOutsCount.setText(String.valueOf(outsCountNum));
    }

    /**
     * Updates the counters based on the item clicked.
     * @param id - The R.id element's id.
     * @param increment - true to increment, false to decrement
     */
    protected void updateCounter(int id, boolean increment){
        switch(id)
        {
            case R.id.strike_button:
            case R.id.strike_linearLayout:
                if(increment && strikeCountNum < 3)
                    strikeCountNum++;   //INFO :: increase the strike count
                else
                    strikeCountNum--;
                if(strikeCountNum < 0)
                    strikeCountNum = 0;
                txtStrikeCount.setText(String.valueOf(strikeCountNum));
                if(strikeCountNum >= 3){
                    outsCountNum++; //INFO :: increase the out number.
                    actionAlert("Out!!");   //INFO :: if it was strike 3, then alert and reset
                }
                break;
            case R.id.ball_button:
            case R.id.ball_linearLayout:
                if(increment && ballCountNum < 4)
                    ballCountNum++; //INFO :: increase the ball count
                else
                    ballCountNum--;
                if(ballCountNum < 0)
                    ballCountNum = 0;
                txtBallCount.setText(String.valueOf(ballCountNum));
                if(ballCountNum >= 4){
                    actionAlert("Walk!!");  //INFO :: if it was ball 4, then alert and reset
                }
                break;
            case R.id.out_linearLayout:
                if(increment)
                    outsCountNum++;
                else
                    outsCountNum--;
                if(outsCountNum < 0)
                    outsCountNum = 0;
                txtOutsCount.setText(String.valueOf(outsCountNum));

            case R.id.about:
                break;

        }

        //INFO :: Persistent Storage update
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(OUTS,outsCountNum); //store the outs in persistent storage
        editor.apply();
    }
    /**
     * Alert the user that a max has been reached, on affirmative the count will be reset.
     * @param call - The message to display
     */
    protected void actionAlert(String call){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.getBoolean(TTS_SELECTION,false)) { //find the user's preference for TTS
            t1.speak(call, TextToSpeech.QUEUE_FLUSH, null, null);
        }
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

    //INFO :: save the instance for the counts when layout changes
    @Override
    protected void onSaveInstanceState(Bundle icicle){
        super.onSaveInstanceState(icicle);
        icicle.putInt(OUTS_COUNT_NAME,outsCountNum);
        icicle.putInt(STRIKES_COUNT_NAME,strikeCountNum);
        icicle.putInt(BALLS_COUNT_NAME,ballCountNum);
    }

}
