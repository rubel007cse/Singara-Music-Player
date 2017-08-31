package com.mrubel.msplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.mrubel.msplayer.ReadStoragePermission.REQUEST_READ_SD_CARD;

public class MoodSelector extends AppCompatActivity {

    LinearLayout _casual, _party, _love, _sad, _happy, _angry;
    TextView t_casual, t_party, t_love, t_sad, t_happy, t_angry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_selector);

        // to make full screen
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        _casual = (LinearLayout) findViewById(R.id.casualmood);
        _party = (LinearLayout) findViewById(R.id.partymood);
        _love = (LinearLayout) findViewById(R.id.lovemood);
        _sad = (LinearLayout) findViewById(R.id.sadmood);
        _happy = (LinearLayout) findViewById(R.id.happymood);
        _angry = (LinearLayout) findViewById(R.id.angrymood);


        t_casual = (TextView) findViewById(R.id.casultracks);
        t_party = (TextView) findViewById(R.id.partytracks);
        t_love = (TextView) findViewById(R.id.lovetracks);
        t_sad = (TextView) findViewById(R.id.sadtracks);
        t_happy = (TextView) findViewById(R.id.happytracks);
        t_angry = (TextView) findViewById(R.id.angrytracks);

        final Intent i = new Intent(MoodSelector.this, MainActivity.class);

        //First checking if the app is already having the permission
        if (ReadStoragePermission.isStorageAccessAllowed(this)) {

            _casual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    i.putExtra("setMood","casual");
                    startActivity(i);
                }
            });

            _party.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    i.putExtra("setMood","party");
                    startActivity(i);
                }
            });


            _love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MoodSelector.this, MainActivity.class);
                    i.putExtra("setMood","love");
                    startActivity(i);
                }
            });


            _sad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MoodSelector.this, MainActivity.class);
                    i.putExtra("setMood","sad");
                    startActivity(i);
                }
            });



            _happy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MoodSelector.this, MainActivity.class);
                    i.putExtra("setMood","happy");
                    startActivity(i);
                }
            });


            _angry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MoodSelector.this, MainActivity.class);
                    i.putExtra("setMood","angry");
                    startActivity(i);
                }
            });





        } else {
            //If the app has not the permission then asking for the permission
            ReadStoragePermission.requestStoragePermission(this);

        }




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == REQUEST_READ_SD_CARD) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                //  Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }


            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getApplicationContext(), "Oops you just denied the permission!", Toast.LENGTH_LONG).show();
            }
        }

    }

}
