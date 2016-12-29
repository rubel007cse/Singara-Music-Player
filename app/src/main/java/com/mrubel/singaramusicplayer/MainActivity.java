package com.mrubel.singaramusicplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView songs;
    ArrayList my_songs = new ArrayList();
    ArrayList song_loc = new ArrayList();
    MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs = (TextView) findViewById(R.id.songs);

        mediaPlayer = new MediaPlayer();

        scanning_SD_card(Environment.getExternalStorageDirectory());


        String s = "";
        for(int i =0; i < my_songs.size(); i++){
            s = s+(i+1)+". "+my_songs.get(i)+"\n\n";
        }
        songs.setText(s);

        play_the_song(song_loc.get(4).toString());
        Log.d("ufkc--off", "/storage/emulated/0/songs/AC_DC - T.N.T..mp3");
        Log.d("ufkc--off", Environment.getExternalStorageDirectory().toString()+my_songs.get(2).toString());
       // Toast.makeText(this, "Playing.. "+ my_songs.get(2).toString(), Toast.LENGTH_SHORT).show();

        Log.d("--my songs---", Arrays.toString(my_songs.toArray()));
        Log.d("--my locc---", Arrays.toString(song_loc.toArray()));

    }


    void scanning_SD_card(File file) {

        File[] files = file.listFiles();

        String loc = "";

        for( File f : files) {

            if(f.isDirectory()){
                loc = loc+f.getName()+"/";
                scanning_SD_card(f);
            } else {

                if((f.getName().endsWith(".mp3")) || (f.getName().endsWith(".acc")) || (f.getName().endsWith(".wav"))){


                    my_songs.add(f.getName());
                    song_loc.add(f.getAbsolutePath());

                }

            }

        }


    }

    void play_the_song(String song) {

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(song));
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
