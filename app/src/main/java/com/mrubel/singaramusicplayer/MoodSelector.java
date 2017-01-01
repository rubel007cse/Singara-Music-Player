package com.mrubel.singaramusicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MoodSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_selector);
    }

    void gocasual(View v){
        switch (v.getId()){
            case R.id.justcasual :
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

        }
    }

}
