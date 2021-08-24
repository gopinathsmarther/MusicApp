package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import me.tankery.lib.circularseekbar.CircularSeekBar;
import smarther.com.musicapp.Activity.RingtoneCutter.RingtoneCutter;
import smarther.com.musicapp.Adapter.PlaylistAddAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.FavouriteModel;
import smarther.com.musicapp.Model.MediaPlayerModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.Model.TopRecentModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Service.BackgroundMusicService;
import smarther.com.musicapp.Utils.Constants;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.MyGestureListener;
import smarther.com.musicapp.Utils.SessionManager;
import smarther.com.musicapp.Utils.SimpleGestureFilter;
import smarther.com.musicapp.databinding.TimerLayoutBinding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
//https://stackoverflow.com/questions/21043059/play-background-sound-in-android-applications
//https://stackoverflow.com/questions/8735931/how-to-implement-in-app-billing-in-an-android-application/8736643
//        https://stackoverflow.com/questions/21872022/notification-for-android-music-player
//        https://stackoverflow.com/questions/12526228/how-to-put-media-controller-button-on-notification-bar/25379408#25379408
//        https://stackoverflow.com/questions/23443946/music-player-control-in-notification
public class SongPlayingActivity extends AppCompatActivity   {
     SimpleGestureFilter detector;
    private GestureDetectorCompat mDetector;
    int millSecond=0;
    TimerLayoutBinding timerLayoutBinding;
    long millisUntilrunning=0;


    CountDownTimer timer = null;

    LinearLayout background_of_layout;
    TextView add_to_playlist_add;
    TextView add_to_playlist_cancel;
    Realm music_database;
    Realm playlist_database;
    RecyclerView playlist_alert_recyclerView;
    PlaylistAddAdapter playlistAddAdapter;
    List<PlaylistAudioModel> playlistAudioModel =new ArrayList<>();
    List<PlaylistModel> playlistModelList =new ArrayList<>();



    TextView song_filename;
    TextView song_size;
    TextView song_path;
    TextView song_format;
    TextView song_length;
    TextView song_bitrateRate;
    TextView song_samplingRate;
    TextView song_tag_edit;
    TextView song_ok;

    LinearLayout close_play_button;
    LinearLayout repeat_song;
    LinearLayout skip_previous_button;
    LinearLayout skip_next_button;
    LinearLayout shuffle_layout;
    LinearLayout song_volume;
    LinearLayout share_app;
    LinearLayout other_option;


    LinearLayout swipe_layout;



    RelativeLayout no_audio_image;
    TextView song_album_name;
    TextView song_name;
    TextView artist_name;
    TextView song_count;
    TextView favourite_icon;
    TextView timing_textview;
    ImageView song_image;
    ImageView queue_music;
    ImageView play_button;
    ImageView song_repeat_imageview;
    ImageView shuffle_image;
    CircularSeekBar circularSeekBar;
    SessionManager sessionManager;
//    MediaPlayer mPlayer;
    String song_mode="0";
    String shuffle="0";
//    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    public static List<AudioModel> audioModelList;
   public static boolean init=true;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    public MusicIntentReceiver myReceiver;
    int intentValue=2;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);
        sessionManager=new SessionManager(SongPlayingActivity.this);
        close_play_button=findViewById(R.id.close_play_button);
        repeat_song=findViewById(R.id.repeat_song);
        skip_previous_button=findViewById(R.id.skip_previous_button);
        skip_next_button=findViewById(R.id.skip_next_button);
        shuffle_image=findViewById(R.id.shuffle_image);
        shuffle_layout=findViewById(R.id.shuffle_layout);
        song_volume=findViewById(R.id.song_volume);
        share_app=findViewById(R.id.share_app);
        no_audio_image=findViewById(R.id.no_audio_image);
        song_album_name=findViewById(R.id.song_album_name);
        song_name=findViewById(R.id.song_name);
        artist_name=findViewById(R.id.artist_name);
        song_count=findViewById(R.id.song_count);
        favourite_icon=findViewById(R.id.favourite_icon);
        timing_textview=findViewById(R.id.timing_textview);
        song_image=findViewById(R.id.song_image);
        play_button=findViewById(R.id.play_button);
        song_repeat_imageview=findViewById(R.id.song_repeat_imageview);
        queue_music=findViewById(R.id.queue_music);
        circularSeekBar=findViewById(R.id.circularSeekBar);
        other_option=findViewById(R.id.other_option);
        swipe_layout=findViewById(R.id.swipe_layout);
        myReceiver = new MusicIntentReceiver();
//        detector = new SimpleGestureFilter(SongPlayingActivity.this, (SimpleGestureFilter.SimpleGestureListener) this);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener(this));

        Intent intent= getIntent();
         intentValue = intent.getIntExtra("value",2);


        try {
            Realm.init(SongPlayingActivity.this);
            RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
            music_database = Realm.getInstance(main_data_config);


            final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
            System.out.println("aji type "+type);
            audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
            play_music();






            close_play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


            FavouriteModel  favouriteModel = music_database.where(FavouriteModel.class).equalTo("track",audioModelList.get(sessionManager.getSongPosition()).getTrack()).findFirst();
            if(favouriteModel!=null)
            {
                favourite_icon.setText(getResources().getString(R.string.fa_heart));

            }
            else
            {
                favourite_icon.setText(getResources().getString(R.string.fa_heart_o));
            }

            queue_music.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson_list = new GsonBuilder().create();
                    JsonArray song_array = gson_list.toJsonTree(audioModelList).getAsJsonArray();
                    sessionManager.setSong_JsonList(song_array.toString());
                    Intent intent=new Intent(SongPlayingActivity.this,PlayingQueue.class);
                    intent.putExtra("title","Playing queue");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });

            skip_previous_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    play_previous_song();

                }
            });
            skip_next_button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    init=true;
                    play_next_song();
                }
            });
            artist_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("aji artist id "+audioModelList.get(sessionManager.getSongPosition()).getArtist_id());
                    sessionManager.setArtistId(Long.parseLong(audioModelList.get(sessionManager.getSongPosition()).getArtist_id()));
                    Global.intent_method(SongPlayingActivity.this, ArtistsDetailsActivity.class);

                }
            });
            other_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)  {
                    Context wrapper = new ContextThemeWrapper(SongPlayingActivity.this, R.style.popupMenuStyle);

//                PopupMenu popup = new PopupMenu(wrapper, view);
                            PopupMenu popup = new PopupMenu(wrapper, other_option);
                            //Inflating the Popup using xml file
                            popup.getMenuInflater().inflate(R.menu.song_playing_popupmenu, popup.getMenu());
                            //registering popup with OnMenuItemClickListener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {

                                        case R.id.add_to_playlist:
                                            add_to_playlist_alert(SongPlayingActivity.this,(sessionManager.getSongPosition()));
                                            //all_playlist(context);
                                            break;
                                        case R.id.go_to_album:
                                            System.out.println("aji album name"+audioModelList.get(sessionManager.getSongPosition()).getAlbum_id());
                                            sessionManager.setAlbumId(Long.parseLong(audioModelList.get(sessionManager.getSongPosition()).getAlbum_id()));
                                            Global.intent_method(SongPlayingActivity.this, AlbumDetailsActivity.class);
                                            break;
                                        case R.id.go_to_artist:
                                            sessionManager.setArtistId(Long.parseLong(audioModelList.get(sessionManager.getSongPosition()).getArtist_id()));
                                            Global.intent_method(SongPlayingActivity.this, ArtistsDetailsActivity.class);
                                            break;
                                        case R.id.share:
                                            shareFile(audioModelList.get(sessionManager.getSongPosition()).getaPath(),audioModelList.get(sessionManager.getSongPosition()).getaAlbumart());
                                            break;
                                        case R.id.tag_editor:
                                            View alertLayout = LayoutInflater.from(SongPlayingActivity.this).inflate(R.layout.song_tag_editor, null);
                                            background_of_layout = alertLayout.findViewById(R.id.background_of_layout);
                                            if(sessionManager.getIsDefaultThemeSelected())
                                            {
                                                background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
                                            }
                                            else
                                            {
                                                Drawable bg;
                                                Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
                                                try {

                                                    File f = new File(Global.getRealPathFromURI(SongPlayingActivity.this,selectedImageUri));
                                                    bg= Drawable.createFromPath(f.getAbsolutePath());

                                                }
                                                catch (Exception e)
                                                {
                                                    bg = ContextCompat.getDrawable(SongPlayingActivity.this, R.drawable.theme_1);
                                                    e.printStackTrace();

                                                }
                                                background_of_layout.setBackground(bg);
                                            }
                                            final AlertDialog.Builder alert = new AlertDialog.Builder(SongPlayingActivity.this);
                                            alert.setCancelable(false);
                                            alert.setView(alertLayout);
                                            alert.setCancelable(true);




                                            final AlertDialog dialog = alert.create();
                                            dialog.show();

                                            break;
                                        case R.id.details:
                                            alert_song_details(sessionManager.getSongPosition());
                                            break;
                                        case R.id.equalizer:
                                            Global.intent_method(SongPlayingActivity.this,EqualizerActivity.class);

                                            break;
                                            case R.id.drive_mode:
                                                Intent i = new Intent(SongPlayingActivity.this, DriveModeActivity.class);
                                                startActivity(i);
                                                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                                            break;

                                        case R.id.sleep_timer:


                                            timerLayoutBinding=  TimerLayoutBinding.inflate(getLayoutInflater());
                                             alertLayout = timerLayoutBinding.getRoot();

                                            if(sessionManager.getIsDefaultThemeSelected())
                                            {
                                                timerLayoutBinding.timerAlertBackground.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
                                            }
                                            else
                                            {
                                                Drawable bg;
                                                Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
                                                try {

                                                    File f = new File(Global.getRealPathFromURI(SongPlayingActivity.this,selectedImageUri));
                                                    bg= Drawable.createFromPath(f.getAbsolutePath());

                                                }
                                                catch (Exception e)
                                                {
                                                    bg = ContextCompat.getDrawable(SongPlayingActivity.this, R.drawable.theme_1);
                                                    e.printStackTrace();
                                                }
                                                timerLayoutBinding.timerAlertBackground.setBackground(bg);
                                            }


                                            if(sessionManager.getInTimerAlready())
                                            {
                                                timerLayoutBinding.currentTimer.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                timerLayoutBinding.currentTimer.setVisibility(View.GONE);
                                            }

                                            timerLayoutBinding.progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                int pval = 0;
                                                @Override
                                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                    pval = progress;

                                                    timerLayoutBinding.timerCount.setText(pval+" min");
                                                }
                                                @Override
                                                public void onStartTrackingTouch(SeekBar seekBar) {
                                                    //write custom code to on start progress
                                                }
                                                @Override
                                                public void onStopTrackingTouch(SeekBar seekBar)
                                                {
                                                    timerLayoutBinding.timerCount.setText(pval+" min");

//                    tView.setText(pval + "/" + seekBar.getMax());
                                                }
                                            });

                                            android.app.AlertDialog.Builder alert1 = new android.app.AlertDialog.Builder(SongPlayingActivity.this);
                                            alert1.setView(alertLayout);
//                alert.setCancelable(false);
                                            final android.app.AlertDialog dialog1 = alert1.create();
                                            dialog1.show();
                                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            timerLayoutBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    dialog1.dismiss();
                                                }
                                            });

                                            timerLayoutBinding.setButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    sessionManager.setInTimerAlready(true);
                                                    if(timer!=null)
                                                    {
                                                        timer.cancel();
                                                    }
                                                    startTimer(timerLayoutBinding.progressSeekBar.getProgress(),dialog1);
                                                    dialog1.dismiss();
                                                }
                                            });



                                        break;


                                        case R.id.ringtone_cutter:
//                                dayString = "Sunday";
                                            Intent intent=new Intent(SongPlayingActivity.this, RingtoneCutter.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("mFilename",audioModelList.get(sessionManager.getSongPosition()).getaPath());
                                            startActivity(intent);
                                            break;
                                        case R.id.delete_from_device:
                                            File file = new File(audioModelList.get(sessionManager.getSongPosition()).getaPath());
                                            file.delete();
                                            deleteFile(file.getName());
                                            if(file.exists()){
                                                try {
                                                    file.getCanonicalFile().delete();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if(file.exists()){
                                                    deleteFile(file.getName());
                                                }
                                            }
//                                File file =new File(audioModelList.get(position).getaPath());
//                                System.out.println("aji + song path "+audioModelList.get(position).getaPath());
//                                if(file.exists())
//                                {
//                                    boolean k = file.delete();
//                                    System.out.println("aji + album del res "+k);
//                                    if(k)
//                                    {
//                                        Toast.makeText(context, ""+audioModelList.get(position).getaName()+" is deleted", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else
//                                        {
//                                        Toast.makeText(context, ""+audioModelList.get(position).getaName()+" is not deleted", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
                                            break;
                                        default:
//                                dayString = "Invalid day";
                                    }
                                    return true;
                                }
                            });

                            popup.show(); //sho
                        }


            });

            song_volume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                }
            });

            favourite_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavouriteModel  favouriteModel = music_database.where(FavouriteModel.class).equalTo("track",audioModelList.get(sessionManager.getSongPosition()).getTrack()).findFirst();
                    if(favouriteModel!=null)
                    {
                        favourite_icon.setText(getResources().getString(R.string.fa_heart_o));
                        sessionManager.setIsFavourite(false);
                        RealmResults<FavouriteModel> result  = music_database.where(FavouriteModel.class).equalTo("track",audioModelList.get(sessionManager.getSongPosition()).getTrack()).findAll();
                        music_database.beginTransaction();
                        result.deleteAllFromRealm();
                        music_database.commitTransaction();
                        Toast.makeText(SongPlayingActivity.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        favourite_icon.setText(getResources().getString(R.string.fa_heart));
                        music_database.beginTransaction();
                        FavouriteModel insert_favourite_song=music_database.createObject(FavouriteModel.class);
                        insert_favourite_song.setaPath(audioModelList.get(sessionManager.getSongPosition()).getaPath());
                        insert_favourite_song.setaName(audioModelList.get(sessionManager.getSongPosition()).getaName());
                        insert_favourite_song.setaAlbum(audioModelList.get(sessionManager.getSongPosition()).getaAlbum());
                        insert_favourite_song.setaArtist(audioModelList.get(sessionManager.getSongPosition()).getaArtist());
                        insert_favourite_song.setaAlbumart(audioModelList.get(sessionManager.getSongPosition()).getaAlbumart());
                        insert_favourite_song.setTrack(audioModelList.get(sessionManager.getSongPosition()).getTrack());
                        music_database.commitTransaction();
                        sessionManager.setIsFavourite(true);
                        Toast.makeText(SongPlayingActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            shuffle_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(shuffle.equals("0"))
                    {
                        shuffle="1";
                        shuffle_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle));
                        Collections.shuffle(audioModelList);
                        sessionManager.setSongPosition(0);
                        Gson gson = new Gson();
                        String json = gson.toJson(audioModelList.get(sessionManager.getSongPosition()));
                        sessionManager.setSong_Json(json);
                        play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
//                        if(mPlayer.isPlaying())
//                        {
//                            mPlayer.stop();
//                        }
                        init=true;
                        play_music();
                    }
                    else
                    {
                        shuffle="0";
                        shuffle_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_grey));
                        audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                        sessionManager.setSongPosition(0);
                        Gson gson = new Gson();
                        String json = gson.toJson(audioModelList.get(sessionManager.getSongPosition()));
                        sessionManager.setSong_Json(json);
                        play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
//                        if(mPlayer.isPlaying())
//                        {
//                            mPlayer.stop();
//                        }
                        init=true;
                        play_music();
                    }
                }
            });


            repeat_song.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(song_mode.equals("0"))
                    {
                        song_mode="1";
                        song_repeat_imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat));
//                        mPlayer.setLooping(false);
                       if(BackgroundMusicService.bg_mediaPlayer!=null)
                       {
                           if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
                           {

                           }

                       }
                        sessionManager.setIsRepeatSong(false);
                        sessionManager.setIsRepeatWholeSongs(true);
                    }
                    else if(song_mode.equals("1"))
                    {
                        song_mode="2";
                        song_repeat_imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_same));
                        if(BackgroundMusicService.bg_mediaPlayer!=null)
                        {
//                            if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
//                            {
//                                BackgroundMusicService.bg_mediaPlayer.setLooping(true);
//                            }

                        }
                        sessionManager.setIsRepeatSong(true);
                        sessionManager.setIsRepeatWholeSongs(false);

                    }
                    else if(song_mode.equals("2"))
                    {
                        song_mode="0";
                        song_repeat_imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_order));
//                        mPlayer.setLooping(false);
                        sessionManager.setIsRepeatSong(false);
                        sessionManager.setIsRepeatWholeSongs(false);
                    }

                }
            });



            circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                    int h = Math.round(progress);
                    //In the above line we are converting the float value into int because
// media player required int value and seekbar library gives progress in float
                    if (BackgroundMusicService.bg_mediaPlayer != null && fromUser)
                    {
                        BackgroundMusicService.bg_mediaPlayer.seekTo(h);
                    }
                    Log.e("abbas","circularSeekBar.setOnSeekBarChangeListener");
                }

                @Override
                public void onStopTrackingTouch(CircularSeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(CircularSeekBar seekBar) {

                }
            });

            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(BackgroundMusicService.bg_mediaPlayer!=null){

                    }
                    if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
                    {
                        init=false;
                        play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    }
                    else
                    {
                        init=false;

                        play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    }
                    Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
                    serviceIntent.setAction(Constants.ACTION.PLAY_ACTION);
                    startService(serviceIntent);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("abbas",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
        }



//        swipe_layout.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View view, MotionEvent event) {
//                int action = MotionEventCompat.getActionMasked(event);
//
//                switch(action) {
//                    case (MotionEvent.ACTION_DOWN) :
//                        Log.d("aji","Action was DOWN");
//                        return true;
//
//                    case (MotionEvent.ACTION_MOVE) :
//                        Log.d("aji","Action was MOVE");
//                        return true;
//
//                    case (MotionEvent.ACTION_UP) :
//                        Log.d("aji","Action was UP");
//                        return true;
//
//                    case (MotionEvent.ACTION_CANCEL) :
//                        Log.d("aji","Action was CANCEL");
//                        return true;
//
//                    case (MotionEvent.ACTION_OUTSIDE) :
//                        Log.d("aji","Movement occurred outside bounds " +
//                                "of current screen element");
//                        return true;
//
//                    default :
//                        return true;
//                }
//            }
//        });

//        final GestureDetector mDetector = new GestureDetector(SongPlayingActivity.this, new MyGestureListener());
//
//        swipe_layout.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(final View v, final MotionEvent event){
//                return mDetector.onTouchEvent(event);
//            }
//        });






    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void add_to_playlist_alert(Context context, final int position){



//        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = LayoutInflater.from(context).inflate(R.layout.add_to_playlist_alert, null);

        Realm.init(context);
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
        music_database = Realm.getInstance(main_data_config);
//        final Dialog alertDialog = new Dialog(context);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.setContentView(R.layout.add_to_playlist_alert);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //  alertDialog.setCancelable(false);
        playlist_alert_recyclerView = alertLayout.findViewById(R.id.playlist_alert_recyclerView);
        background_of_layout = alertLayout.findViewById(R.id.background_of_layout);
        add_to_playlist_add = alertLayout.findViewById(R.id.add_to_playlist_add);
        add_to_playlist_cancel = alertLayout.findViewById(R.id.add_to_playlist_cancel);
//        playlist_alert_recyclerView = alertDialog.findViewById(R.id.playlist_alert_recyclerView);
//        background_of_layout = alertDialog.findViewById(R.id.background_of_layout);
//        add_to_playlist_add = alertDialog.findViewById(R.id.add_to_playlist_add);
//        add_to_playlist_cancel = alertDialog.findViewById(R.id.add_to_playlist_cancel);
        sessionManager=new SessionManager(context);
        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(context,selectedImageUri));
                bg= Drawable.createFromPath(f.getAbsolutePath());


//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(context, R.drawable.theme_1);
                e.printStackTrace();

            }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
            background_of_layout.setBackground(bg);
        }
        playlist_alert_recyclerView.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false));
        playlist_alert_recyclerView.setHasFixedSize(true);
        playlist_alert_recyclerView.setNestedScrollingEnabled(false);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setView(alertLayout);

//        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });

        all_playlist(context);
        final AlertDialog dialog = alert.create();
        dialog.show();
//        alertDialog.show();
        add_to_playlist_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playlistAddAdapter.playlistId_list.size()>0)
                {
                    for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){


                        playlist_database.beginTransaction();

                        PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);

                        // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                        long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));
                        playlistAudioModel.setPlaylist_id(a);
                        playlistAudioModel.setaPath(audioModelList.get(position).getaPath());
                        playlistAudioModel.setaName(audioModelList.get(position).getaName());
                        playlistAudioModel.setaAlbum(audioModelList.get(position).getaAlbum());
                        playlistAudioModel.setAlbum_id(audioModelList.get(position).getAlbum_id());
                        playlistAudioModel.setaArtist(audioModelList.get(position).getaArtist());
                        playlistAudioModel.setArtist_id(audioModelList.get(position).getArtist_id());
                        playlistAudioModel.setTrack(audioModelList.get(position).getTrack());
                        playlistAudioModel.setLength(audioModelList.get(position).getLength());
                        playlistAudioModel.setSong_added_on(audioModelList.get(position).getSong_added_on());
                        playlist_database.commitTransaction();
                        playlistAddAdapter.notifyDataSetChanged();
                        System.out.println("aji "+i+" pl_ID "+ playlistAddAdapter.playlistId_list.get(i));
                        System.out.println("aji "+playlist_database.toString());

                    }

                }
                dialog.dismiss();


            }
        });
        add_to_playlist_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
    public void all_playlist(Context context)
    {
        playlistModelList = music_database.where(PlaylistModel.class).findAll();
//        playlistAudioModel = playlist_database.where(PlaylistAudioModel.class).findAll();
//                .sort("dateTimeStart", Sort.DESCENDING);
        if(playlistModelList.size()==0)
        {
//            playlist_count.setVisibility(View.GONE);
            Number currentIdNum = music_database.where(PlaylistModel.class).max("playlist_id");
            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            System.out.println("aji nextId "+nextId);
            music_database.beginTransaction();
            PlaylistModel playlistModel=music_database.createObject(PlaylistModel.class,nextId);
            playlistModel.setPlaylist_name("Favourites");
            music_database.commitTransaction();
        }
        else
        {
//            playlist_count.setText(""+playlistModelList.size());
        }
        playlistAddAdapter=new PlaylistAddAdapter(context,playlistModelList);
        playlist_alert_recyclerView.setAdapter(playlistAddAdapter);


    }


    private void shareFile(String filePath,String album_filepath) {

        File f = new File(filePath);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(filePath);

        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("text/*");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, f.getName());
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "-Shared via Muzio Player");
            startActivity(Intent.createChooser(intentShareFile, f.getName()));
        }
    }
    public void alert_song_details(int position){
        View alertLayout = LayoutInflater.from(SongPlayingActivity.this).inflate(R.layout.song_details_alert, null);
        background_of_layout = alertLayout.findViewById(R.id.background_of_layout);
        song_filename = alertLayout.findViewById(R.id.song_filename);
        song_path = alertLayout.findViewById(R.id.song_path);
        song_size = alertLayout.findViewById(R.id.song_size);
        song_ok = alertLayout.findViewById(R.id.song_ok);
        song_length = alertLayout.findViewById(R.id.song_length);

        try {

            Uri uri = Uri.parse(audioModelList.get(position).getaPath());
            System.out.println("aji play q path "+audioModelList.get(position).getaPath());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(SongPlayingActivity.this,uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            millSecond = millSecond + Integer.parseInt(durationStr);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(SongPlayingActivity.this,selectedImageUri));
                bg= Drawable.createFromPath(f.getAbsolutePath());

            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(SongPlayingActivity.this, R.drawable.theme_1);
                e.printStackTrace();

            }
            background_of_layout.setBackground(bg);
        }


        song_filename.setText(audioModelList.get(position).getaName());
        song_path.setText(audioModelList.get(position).getaPath());
        song_length.setText(""+String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millSecond), TimeUnit.MILLISECONDS.toMinutes(millSecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecond)), TimeUnit.MILLISECONDS.toSeconds(millSecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond))));

        final AlertDialog.Builder alert = new AlertDialog.Builder(SongPlayingActivity.this);
        alert.setCancelable(false);
        alert.setView(alertLayout);




        final AlertDialog dialog = alert.create();
        dialog.show();
        song_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }




//    private Runnable UpdateSongTime=new Runnable() {
//        @Override
//        public void run() {
//            long minutes = (BackgroundMusicService.bg_mediaPlayer.getCurrentPosition() / 1000) / 60;
//            long seconds = (BackgroundMusicService.bg_mediaPlayer.getCurrentPosition() / 1000) % 60;
//                String minutes_string=String.valueOf(minutes);
//                String seconds_string=String.valueOf(seconds);
//
//            long song_minutes = (BackgroundMusicService.bg_mediaPlayer.getDuration() / 1000) / 60;
//            long song_seconds = (BackgroundMusicService.bg_mediaPlayer.getDuration()/ 1000) % 60;
//                String song_minutes_string=String.valueOf(song_minutes);
//                String song_seconds_string=String.valueOf(song_seconds);
//
//
//                if(minutes_string.length()==1)
//                {
//                    minutes_string="0"+minutes;
//
//                }
//                else
//                {
//                    minutes_string=""+minutes;
//                }
//                if(seconds_string.length()==1)
//                {
//                    seconds_string="0"+seconds;
//
//                }
//                else
//                {
//                    seconds_string=""+seconds;
//                }
//                if(song_minutes_string.length()==1)
//                {
//                    song_minutes_string="0"+song_minutes;
//
//                }
//                else
//                {
//                    song_minutes_string=""+song_minutes;
//                }
//                if(song_seconds_string.length()==1)
//                {
//                    song_seconds_string="0"+song_seconds;
//
//                }
//                else
//                {
//                    song_seconds_string=""+song_seconds;
//                }
//
//
//                timing_textview.setText(minutes_string+" : "+seconds_string+" | "+song_minutes_string+" : "+song_seconds_string);
//
//            circularSeekBar.setProgress(BackgroundMusicService.bg_mediaPlayer.getCurrentPosition());
//            if(BackgroundMusicService.bg_mediaPlayer.getCurrentPosition()==BackgroundMusicService.bg_mediaPlayer.getDuration())
//                {
//                    play_next_song();
//                }
//                hdlr.postDelayed(this, 100);
//            }
//    };

    public void on_song_play_UiChange(String value)
    {
        try {
//            int current_song_count=0;
//            if(value.equalsIgnoreCase("1"))
//            {
//                 current_song_count=sessionManager.getSongPosition();
//            }
//            else
//            {
//               current_song_count=sessionManager.getSongPosition()+1;
//            }
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
            System.out.println("aji apath 1 "+jsonObject.optString("aPath"));
            play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));

            FavouriteModel  favouriteModel = music_database.where(FavouriteModel.class).equalTo("track",audioModelList.get(sessionManager.getSongPosition()).getTrack()).findFirst();
            if(favouriteModel!=null)
            {
                favourite_icon.setText(getResources().getString(R.string.fa_heart));

            }
            else
            {
                favourite_icon.setText(getResources().getString(R.string.fa_heart_o));
            }
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
                    favourite_icon.setText(getResources().getString(R.string.fa_heart));
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

    public void play_music()
    {
        try {
            on_song_play_UiChange("1");
           // if(getIntent().getIntExtra("value")==1)
            {
                int current_song_count = sessionManager.getSongPosition() + 1;
                JSONObject jsonObject = new JSONObject(sessionManager.getSong_Json());
                System.out.println("aji apath " + jsonObject.optString("aPath"));
                if(intentValue==1){
                    System.out.println("aji intent 1");
                    if( BackgroundMusicService.bg_mediaPlayer!=null){
                        System.out.println("aji intent playing");
                        Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
                        serviceIntent.setAction(Constants.ACTION.PAUSE);
                        startService(serviceIntent);
                    }
                    else {
                        System.out.println("aji at pause");
                        Intent stopserviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class); //added by aji
                        stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                        stopService(stopserviceIntent);
                        System.out.println("aji after pause");
                        Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
                        serviceIntent.putExtra("song_name", jsonObject.optString("track"));
                        serviceIntent.putExtra("artist_name", jsonObject.optString("aArtist"));
                        serviceIntent.putExtra("song_count", current_song_count + "/" + audioModelList.size());
                        serviceIntent.putExtra("album_art", jsonObject.optString("aAlbumart"));
                        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                        startService(serviceIntent);

                    }
                }
                else {
                    Intent stopserviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class); //added by aji
                    stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                    stopService(stopserviceIntent);
                    Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
                    serviceIntent.putExtra("song_name", jsonObject.optString("track"));
                    serviceIntent.putExtra("artist_name", jsonObject.optString("aArtist"));
                    serviceIntent.putExtra("song_count", current_song_count + "/" + audioModelList.size());
                    serviceIntent.putExtra("album_art", jsonObject.optString("aAlbumart"));
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(serviceIntent);
                }

            }
//            progressHandler();
    } catch (JSONException e) {
        e.printStackTrace();
    }
    }

    public void play_next_song()
    {
        System.out.println("aji nextsong");
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
//            String json = gson.toJson( audioModelList.get(sessionManager.getSongPosition()));

            String json = gson.toJson( audioModelList.get(sessionManager.getSongPosition()));
            sessionManager.setSong_Json(json);
            play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            on_song_play_UiChange("2");
            int current_song_count=sessionManager.getSongPosition()+1;
            JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
            Intent stopserviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
            stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            stopService(stopserviceIntent);
            Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
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
              play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
              on_song_play_UiChange("2");
              int current_song_count=sessionManager.getSongPosition()+1;
              JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
              Intent stopserviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
              stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
              stopService(stopserviceIntent);
              Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
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


    void startTimer(int minutes, final android.app.AlertDialog dialog)
    {
        final long millis = minutes * 60 * 1000;
        millisUntilrunning=millis+millisUntilrunning;
        timer = new CountDownTimer(millisUntilrunning, 1000) {
            public void onTick(long millisUntilFinished)
            {
                String milis=""+millisUntilFinished / 1000;
                if(milis.length()==1)
                {
                    timerLayoutBinding.currentTimer.setText(" 0"+millisUntilFinished / 1000);
                }
                else
                {
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    Log.e("abbas",""+millis);
                    Log.e("abbas",""+millisUntilFinished);
                    Log.e("abbas",hms);
                    timerLayoutBinding.currentTimer.setText(hms);
                }

//                timerLayoutBinding.currentTimer.setText("Current Timer "+milis);

            }
            public void onFinish()
            {
                if(timer!=null)
                    timer.cancel();
                sessionManager.setInTimerAlready(false);
                dialog.dismiss();
                Intent stopserviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
                stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                stopService(stopserviceIntent);
            }
        };
        timer.start();
    }

//    public void progressHandler()
//    {
//        hdlr.postDelayed(UpdateSongTime, 100);
//
//        BackgroundMusicService.bg_mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//
//
//            }
//        });
//
//    }

    @Subscribe
    public void onEvent(MediaPlayerModel mediaPlayerModel)
    {
        /* event fire here when you post event from other class or fragment */
        if(mediaPlayerModel!=null)
        {
            if(mediaPlayerModel.getType()==1)
            {
                Log.e("abbas","onEvent  Called");
                timing_textview.setText(mediaPlayerModel.getSong_timing());
                circularSeekBar.setProgress(mediaPlayerModel.getSong_position());
                circularSeekBar.setMax((float) BackgroundMusicService.bg_mediaPlayer.getDuration());
            }
            else if(mediaPlayerModel.getType()==2)
            {
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
            else if(mediaPlayerModel.getType()==3)
            {
                int current_song_count=sessionManager.getSongPosition()+1;
                try
                {
                    JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
                    Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
                    serviceIntent.putExtra("song_name",jsonObject.optString("track"));
                    serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
                    serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
                    serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(serviceIntent);
                }
                catch (Exception e)
                {

                }

            }
            else if(mediaPlayerModel.getType()==4)
            {

                sessionManager.setSongPosition(0);
                int current_song_count=sessionManager.getSongPosition()+1;
                Gson gson = new Gson();
                String json = gson.toJson(audioModelList.get(sessionManager.getSongPosition()));
                sessionManager.setSong_Json(json);
                on_song_play_UiChange("1");
                try
                {
                    JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
                    Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
                    serviceIntent.putExtra("song_name",jsonObject.optString("track"));
                    serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
                    serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
                    serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(serviceIntent);
                }
                catch (Exception e)
                {

                }

            }

        }


    };


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);//Register
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);//unregister
    }

    protected void onDestroy() {
        //stop service and stop music
//        stopService(new Intent(SongPlayingActivity.this, BackgroundMusicService.class));
//        Intent serviceIntent = new Intent(SongPlayingActivity.this, BackgroundMusicService.class);
//        serviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
//        stopService(serviceIntent);
        super.onDestroy();
    }
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
