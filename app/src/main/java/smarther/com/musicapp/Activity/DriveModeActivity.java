package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.FavouriteModel;
import smarther.com.musicapp.Model.TopRecentModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Service.BackgroundMusicService;
import smarther.com.musicapp.Utils.Constants;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DriveModeActivity extends AppCompatActivity {
    public MusicIntentReceiver myReceiver;

    LinearLayout close_play_button;
    LinearLayout previous_music;
    LinearLayout next_music;
    LinearLayout play_music;
    RelativeLayout no_audio_image;
    ImageView song_image;
    ImageView play_song_image;
    SeekBar seekBar;
    TextView song_name;
    TextView song_album_name;
    TextView artist_name;
    TextView song_count;
    TextView timing_textview;
    TextView full_timing_textview;

    boolean init=true;
    Realm music_database;
    List<AudioModel> audioModelList;
//    MediaPlayer mPlayer;
    SessionManager sessionManager;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    Date date = new Date();
    private Handler hdlr = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_mode);
        close_play_button=findViewById(R.id.close_play_button);
        previous_music=findViewById(R.id.previous_music);
        next_music=findViewById(R.id.next_music);
        final LinearLayout play_music =findViewById(R.id.play_music);
        no_audio_image =findViewById(R.id.no_audio_image);
        play_song_image=findViewById(R.id.play_song_image);
        seekBar=findViewById(R.id.seekBar);
        song_name=findViewById(R.id.song_name);
        song_album_name=findViewById(R.id.song_album_name);
        artist_name=findViewById(R.id.artist_name);
        song_count=findViewById(R.id.song_count);
        timing_textview=findViewById(R.id.timing_textview);
        full_timing_textview=findViewById(R.id.full_timing_textview);
        song_image=findViewById(R.id.song_image);
        Realm.init(DriveModeActivity.this);
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
        music_database = Realm.getInstance(main_data_config);
        sessionManager=new SessionManager(DriveModeActivity.this);
        myReceiver = new MusicIntentReceiver();

        final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
        audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);

        play_music();
//        if(mPlayer.isPlaying())
//        {
//            play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
//        }
//        else
//        {
//            play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
//        }
        close_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottom_layout.dismiss();
                Intent i = new Intent(DriveModeActivity.this, HomeScreen.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_up_reverse);
            }
        });
        previous_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                play_previous_song();

            }
        });
        next_music.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                init=true;
                play_next_song();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int h = Math.round(progress);
                //In the above line we are converting the float value into int because
// media player required int value and seekbar library gives progress in float
                if (BackgroundMusicService.bg_mediaPlayer != null && fromUser) {
                    BackgroundMusicService.bg_mediaPlayer.seekTo(h);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        play_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
                {
                    init=false;
                    play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                }
                else
                {
                    init=false;
                    play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                }
                Intent serviceIntent = new Intent(DriveModeActivity.this, BackgroundMusicService.class);
                serviceIntent.setAction(Constants.ACTION.PLAY_ACTION);
                startService(serviceIntent);
            }
        });
    }


    public void play_next_song()
    {
        try
        {
            if(sessionManager.getSongPosition()+1==audioModelList.size())
            {

            }
            else
            {

                int next_song_position=sessionManager.getSongPosition();
                next_song_position=next_song_position+1;
                sessionManager.setSongPosition(next_song_position);
                Gson gson = new Gson();
                String json = gson.toJson( audioModelList.get(sessionManager.getSongPosition()));
                sessionManager.setSong_Json(json);
                play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                on_song_play_UiChange();
                int current_song_count=sessionManager.getSongPosition()+1;
                JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
                Intent stopserviceIntent = new Intent(DriveModeActivity.this, BackgroundMusicService.class);
                stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                stopService(stopserviceIntent);
                Intent serviceIntent = new Intent(DriveModeActivity.this, BackgroundMusicService.class);
                serviceIntent.putExtra("song_name",jsonObject.optString("track"));
                serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
                serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
                serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
                serviceIntent.setAction(Constants.ACTION.NEXT_ACTION);
                startService(serviceIntent);
                //progressHandler();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void play_previous_song()
    {
        try
        {
            if(sessionManager.getSongPosition()==0)
            {

            }
            else
            {

                int next_song_position=sessionManager.getSongPosition();
                next_song_position=next_song_position-1;
                sessionManager.setSongPosition(next_song_position);
                Gson gson = new Gson();
                String json = gson.toJson( audioModelList.get(sessionManager.getSongPosition()));
                sessionManager.setSong_Json(json);
                play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                on_song_play_UiChange();
                int current_song_count=sessionManager.getSongPosition()+1;
                JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
                Intent stopserviceIntent = new Intent(DriveModeActivity.this, BackgroundMusicService.class);
                stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                stopService(stopserviceIntent);
                Intent serviceIntent = new Intent(DriveModeActivity.this, BackgroundMusicService.class);
                serviceIntent.putExtra("song_name",jsonObject.optString("track"));
                serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
                serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
                serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
                serviceIntent.setAction(Constants.ACTION.PREV_ACTION);
                startService(serviceIntent);
                //progressHandler();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void play_music()
    {
        try {
            on_song_play_UiChange();
            int current_song_count=sessionManager.getSongPosition()+1;
            JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
            Intent serviceIntent = new Intent(DriveModeActivity.this, BackgroundMusicService.class);
            serviceIntent.putExtra("song_name",jsonObject.optString("track"));
            serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
            serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
            serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(serviceIntent);
            seekBar.setProgress(sTime);
            hdlr.postDelayed(UpdateSongTime, 100);

            if( BackgroundMusicService.bg_mediaPlayer!=null)
            {
                BackgroundMusicService.bg_mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try
                        {
                            init=true;
                            play_next_song();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

            }

//            progressHandler();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void on_song_play_UiChange()
    {
        try {
            int current_song_count=sessionManager.getSongPosition()+1;
            song_count.setText(current_song_count+"/"+audioModelList.size());
            JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
            song_album_name.setText(jsonObject.optString("aAlbum"));
            song_name.setText(jsonObject.optString("track"));
            artist_name.setText(jsonObject.optString("aArtist"));

            String album_art=jsonObject.optString("aAlbumart");
            if(album_art==null || album_art.equals(""))
            {
                song_image.setVisibility(View.GONE);
                no_audio_image.setVisibility(View.VISIBLE);
            }
            else
            {
                no_audio_image.setVisibility(View.GONE);
                song_image.setVisibility(View.VISIBLE);
                Bitmap bm= BitmapFactory.decodeFile(album_art);
                song_image.setImageBitmap(bm);
            }
            sessionManager.setBackgroundMusic(jsonObject.optString("aPath"));
            play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));


            if(init)
            {
                TopRecentModel topRecentModel = music_database.where(TopRecentModel.class).equalTo("track",audioModelList.get(sessionManager.getSongPosition()).getTrack()).findFirst();
                if(topRecentModel!=null)
                {
                    music_database.beginTransaction();
                    int increase_count= topRecentModel.getPlay_count()+1;
                    topRecentModel.setPlay_count(increase_count);
                    topRecentModel.setPlayed_time(formatter.format(date));
                    music_database.commitTransaction();

                }
                else
                {
                    music_database.beginTransaction();
                    TopRecentModel insert_topRecentModel_song=music_database.createObject(TopRecentModel.class);
                    insert_topRecentModel_song.setaPath(audioModelList.get(sessionManager.getSongPosition()).getaPath());
                    insert_topRecentModel_song.setaName(audioModelList.get(sessionManager.getSongPosition()).getaName());
                    insert_topRecentModel_song.setaAlbum(audioModelList.get(sessionManager.getSongPosition()).getaAlbum());
                    insert_topRecentModel_song.setaArtist(audioModelList.get(sessionManager.getSongPosition()).getaArtist());
                    insert_topRecentModel_song.setaAlbumart(audioModelList.get(sessionManager.getSongPosition()).getaAlbumart());
                    insert_topRecentModel_song.setTrack(audioModelList.get(sessionManager.getSongPosition()).getTrack());
                    insert_topRecentModel_song.setArtist_id(audioModelList.get(sessionManager.getSongPosition()).getArtist_id());
                    insert_topRecentModel_song.setPlay_count(1);
                    insert_topRecentModel_song.setPlayed_time(formatter.format(date));
                    music_database.commitTransaction();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            sTime = BackgroundMusicService.bg_mediaPlayer.getCurrentPosition();
//            timing_textview.setText(String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(sTime),TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );

            long minutes = (BackgroundMusicService.bg_mediaPlayer.getCurrentPosition() / 1000) / 60;
            long seconds = (BackgroundMusicService.bg_mediaPlayer.getCurrentPosition() / 1000) % 60;
            String minutes_string=String.valueOf(minutes);
            String seconds_string=String.valueOf(seconds);

            long song_minutes = (BackgroundMusicService.bg_mediaPlayer.getDuration() / 1000) / 60;
            long song_seconds = (BackgroundMusicService.bg_mediaPlayer.getDuration()/ 1000) % 60;
            String song_minutes_string=String.valueOf(song_minutes);
            String song_seconds_string=String.valueOf(song_seconds);


            if(minutes_string.length()==1)
            {
                minutes_string="0"+minutes;

            }
            else
            {
                minutes_string=""+minutes;
            }
            if(seconds_string.length()==1)
            {
                seconds_string="0"+seconds;

            }
            else
            {
                seconds_string=""+seconds;
            }
            if(song_minutes_string.length()==1)
            {
                song_minutes_string="0"+song_minutes;

            }
            else
            {
                song_minutes_string=""+song_minutes;
            }
            if(song_seconds_string.length()==1)
            {
                song_seconds_string="0"+song_seconds;

            }
            else
            {
                song_seconds_string=""+song_seconds;
            }


            timing_textview.setText(minutes_string+" : "+seconds_string);
            full_timing_textview.setText(song_minutes_string+" : "+song_seconds_string);

            seekBar.setProgress(sTime);
            seekBar.setMax(BackgroundMusicService.bg_mediaPlayer.getDuration());
            if(BackgroundMusicService.bg_mediaPlayer.getCurrentPosition()==BackgroundMusicService.bg_mediaPlayer.getDuration())
            {
                if(sessionManager.getSongPosition()+1==audioModelList.size())
                {

                }
                else
                {
                    int next_song_position=sessionManager.getSongPosition();
                    next_song_position=next_song_position+1;
                    sessionManager.setSongPosition(next_song_position);
                    Gson gson = new Gson();
                    String json = gson.toJson( audioModelList.get(sessionManager.getSongPosition()));
                    sessionManager.setSong_Json(json);
                    play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    play_music();
                }

            }
            hdlr.postDelayed(this, 100);
        }
    };
    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }
    @Override public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

}
