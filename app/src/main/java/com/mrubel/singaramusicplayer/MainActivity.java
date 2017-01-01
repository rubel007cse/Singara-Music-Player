package com.mrubel.singaramusicplayer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ListView songs;
    ArrayList my_songs = new ArrayList();
    ArrayList song_loc = new ArrayList();
    MediaPlayer mediaPlayer = null;
    int final_pos;
    TextView tv_timer;
    ProgressBar progressBar;
    int progress_t, res,time,min,sec;
    long song_duration;
    double duro;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs = (ListView) findViewById(R.id.songs);
        tv_timer = (TextView) findViewById(R.id.mytimer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(
                Color.parseColor("#ffffff"), android.graphics.PorterDuff.Mode.SRC_IN);

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
                Toast.makeText(getApplicationContext(), "Playing Singara "+ my_songs.get(position), Toast.LENGTH_LONG).show();

                // playing next song
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        // go to next in on completion
                        if((final_pos+1) == my_songs.size()){
                            play_the_song(song_loc.get(0)+"");
                            Toast.makeText(getApplicationContext(), "Playing Singara "+ my_songs.get(0), Toast.LENGTH_LONG).show();
                            final_pos = 1;
                        } else {
                            play_the_song(song_loc.get(final_pos)+"");
                            Toast.makeText(getApplicationContext(), "Playing Singara "+ my_songs.get(final_pos), Toast.LENGTH_LONG).show();
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
        res = 0; time = 0; min =0; sec =0;
        progressBar.setProgress(0);

        try {
            mediaPlayer.setDataSource(this, Uri.parse(song));
            mediaPlayer.prepare();
            mediaPlayer.start();

            song_duration= mediaPlayer.getDuration();

            // show this amount of progress in every second
            duro = (double) 100/ (song_duration/1000);


            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                tv_timer.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        time = (int) (song_duration/1000)-(mediaPlayer.getCurrentPosition()/1000);
                                        min = time/60;
                                        sec = time%60;
                                        tv_timer.setText("Time Left: "+min+" : "+sec);
                                        res = (int)(duro*progress_t);
                                        progressBar.setProgress(res);
                                        Log.d("--pt---", res+"");
                                        progress_t += 1;
                                    }
                                });
                            } else {
                                timer.cancel();
                                timer.purge();
                            }
                        }
                    });
                }
            }, 0, 1000);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
