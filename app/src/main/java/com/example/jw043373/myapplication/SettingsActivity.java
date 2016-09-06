package com.example.jw043373.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PrefsFile";
    private static final String TTS_SELECTION = "TTS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        Switch tts = (Switch) findViewById(R.id.ttsSwitch);
        tts.setChecked(settings.getBoolean(TTS_SELECTION,false));

        tts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(TTS_SELECTION,isChecked);
                editor.apply();
            }
        });

        Button accept = (Button) findViewById(R.id.acceptChanges);
        accept.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                finish();
            }
        });
    }
}
