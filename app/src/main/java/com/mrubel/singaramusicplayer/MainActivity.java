package com.mrubel.singaramusicplayer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView songs;
    ArrayList my_songs = new ArrayList();
    ArrayList song_loc = new ArrayList();
    MediaPlayer mediaPlayer = null;
    int final_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs = (ListView) findViewById(R.id.songs);

        mediaPlayer = new MediaPlayer();

        // fetching and getting songs from sd card to arraylist
        scanning_SD_card(Environment.getExternalStorageDirectory());

        // showing song list
        songs.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.list_view_lay, R.id.solo_song, my_songs));

        // listview click
        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final_pos = position;
                play_the_song(song_loc.get(position)+"");
                Toast.makeText(getApplicationContext(), "Playing Singara "+ my_songs.get(position), Toast.LENGTH_SHORT).show();

                // plaing next song
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        // go to next in on completion
                        if((final_pos+1) == my_songs.size()){
                            play_the_song(song_loc.get(0)+"");
                            Toast.makeText(getApplicationContext(), "Playing Singara "+ my_songs.get(0), Toast.LENGTH_SHORT).show();
                            final_pos = 1;
                        } else {
                            play_the_song(song_loc.get(final_pos)+"");
                            Toast.makeText(getApplicationContext(), "Playing Singara "+ my_songs.get(final_pos), Toast.LENGTH_SHORT).show();
                            final_pos += 1;
                        }
                    }
                });


                // changing bg color
                for (int i = 0; i < songs.getChildCount(); i++) {
                    if(position == i ){
                        songs.getChildAt(i).setBackgroundColor(Color.parseColor("#E0E0E0"));
                    }else{
                        songs.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

            }
        });
    }


    void scanning_SD_card(File file) {

        File[] files = file.listFiles();

        for( File f : files) {

            if(f.isDirectory()){
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
