package com.mrubel.msplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ListView songs;
    List<String> my_songs = new ArrayList<String>();
    ArrayList<Bitmap> albumArts = new ArrayList<Bitmap>();
    ArrayList song_loc = new ArrayList();
    MediaPlayer mediaPlayer = null;
    int final_pos;
    TextView tv_timer;
    ProgressBar progressBar;
    int progress_t, res,time,min,sec;
    long song_duration;
    double duro;
    Timer timer;
    ImageView albumAlt;
    MediaMetadataRetriever metaRetriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // to make full screen
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // rec intent
        String get_mood = getIntent().getStringExtra("setMood");
        Log.d("get_mood", get_mood);

        songs = (ListView) findViewById(R.id.songs);
        tv_timer = (TextView) findViewById(R.id.mytimer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(
                Color.parseColor("#ffffff"), android.graphics.PorterDuff.Mode.SRC_IN);

        mediaPlayer = new MediaPlayer();

        albumAlt = (ImageView) findViewById(R.id.albumart);

        // fetching and getting songs from sd card to arraylist
       // scanning_SD_card(Environment.getExternalStorageDirectory());

        Log.d("Loaded songs", ""+my_songs.toString());

        // showing song list
     /*   songs.setAdapter(new ArrayAdapter(MainActivity.this,
                R.layout.list_view_lay,
                R.id.solo_song, my_songs));
*/
        getSongList();


        String[] songsArray = my_songs.toArray(new String[my_songs.size()]);
        Bitmap[] aartsArray = albumArts.toArray(new Bitmap[albumArts.size()]);

        songs.setAdapter(new CustomAdapter(MainActivity.this, songsArray, aartsArray));

        // list view click to play the song
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

                  //  Log.d("Each song ", f.getName());

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

            // for total time
            final int seconds = (int) (song_duration / 1000) % 60 ;
            final int minutes = (int) ((song_duration / (1000*60)) % 60);


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
                                        tv_timer.setText(addDigit(min)+":"+addDigit(sec)+"/"+addDigit(minutes)+":"+addDigit(seconds));
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


    public void getSongList() {
        // retrieve song info

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null,
                null);


        if (musicCursor != null && musicCursor.moveToFirst()) {
            // get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.MediaColumns.TITLE);
            int idColumn = musicCursor.getColumnIndex(BaseColumns._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
            int column_index = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

            // add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String pathId = musicCursor.getString(column_index);
                Log.d(this.getClass().getName(), "path id=" + pathId);
                song_loc.add(pathId);

                metaRetriver = new MediaMetadataRetriever();
                metaRetriver.setDataSource(pathId);
                try {
                    byte[] art = metaRetriver.getEmbeddedPicture();
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 2;
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length, opt);

                   // albumAlt.setImageBitmap(songImage);
                    albumArts.add(songImage);

                } catch (Exception e) {
                    //imgAlbumArt.setBackgroundColor(Color.GRAY);
                }

                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                Log.d("Song det: ", thisId + ", " + thisTitle + ", " + thisArtist);

                my_songs.add(thisId + ", " + thisTitle + ", " + thisArtist);

            } while (musicCursor.moveToNext());

        }
    }

    String addDigit(int val){
        String s = val+"";
      //  Log.d("Len: ","Value: "+s+", Length: "+s.length());
        if(s.length() == 1){
            return "0"+s;
        } else {
            return s+"";
        }
    }

}
