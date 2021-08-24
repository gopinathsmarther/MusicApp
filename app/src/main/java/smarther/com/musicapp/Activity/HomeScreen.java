package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import io.realm.Realm;
import smarther.com.musicapp.Activity.RingtoneCutter.RCSongSelectionActivity;
import smarther.com.musicapp.Adapter.MyPagerAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Fragments.AlbumFragment;
import smarther.com.musicapp.Fragments.ArtistsFragment;
import smarther.com.musicapp.Fragments.PlaylistFragment;
import smarther.com.musicapp.Fragments.SongsFragment;
import smarther.com.musicapp.Model.AlbumModel;
import smarther.com.musicapp.Model.ArtistsModel;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.TopRecentModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Service.BackgroundMusicService;
import smarther.com.musicapp.Utils.Constants;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;
import smarther.com.musicapp.Utils.ShakeDetector;
import smarther.com.musicapp.databinding.TimerLayoutBinding;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeScreen extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, Animator.AnimatorListener {


    private boolean mIsInAnimation;
    private long mMotionBeginTime;
    private float mLastMotionX;
    ObjectAnimator anim;


    LinearLayout background_of_layout;
    LinearLayout suggested_layout;
    LinearLayout songs_layout;
    LinearLayout albums_layout;
    LinearLayout other_option;
    LinearLayout artist_layout;
    LinearLayout playlist_layout;
    LinearLayout folders_layout;
    LinearLayout genres_layout;
    LinearLayout grid_1;
    LinearLayout grid_2;
    LinearLayout grid_3;
    LinearLayout grid_4;
    LinearLayout default_style;
    LinearLayout circular_style;
    LinearLayout ascending;
    LinearLayout descending;
    LinearLayout artist;
    LinearLayout album;
    LinearLayout year_sort;

    HorizontalScrollView horizontalScrollView;
    TextView suggested_textview;
    TextView songs_textview;
    TextView albums_textview;
    TextView artist_textview;
    TextView playlist_textview;
    TextView folders_textview;
    TextView genres_textview;
    TextView search_navigation;

    View suggested_view;
    View songs_view;
    View albums_view;
    View artist_view;
    View playlist_view;
    View folders_view;
    View genres_view;

    SessionManager sessionManager;
    NavigationView navigationView;

    TimerLayoutBinding timerLayoutBinding;

    CountDownTimer timer = null;
    SongsAdapter songsAdapter;
    long millisUntilrunning=0;

    BottomSheetDialog bottom_layout;
    Realm music_database;
    List<AudioModel> audioModelList;
    MediaPlayer mPlayer;
    private Handler hdlr = new Handler();
    boolean init=true;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    LinearLayout close_play_button;
    LinearLayout previous_music;
    LinearLayout next_music;
    LinearLayout play_music;
    RelativeLayout no_audio_image;
    ImageView song_image;
    ImageView play_song_image;

    SeekBar seekBar;


    public static TextView playing_song_name;
    public static TextView playing_song_artist;
    public static TextView playing_song_folder;
    public static ImageView play_button;
    LinearLayout queue_music1;
    LinearLayout currentSongpage;
    JSONObject jsonObject;

    List<AudioModel> audioModelList1;





    TextView song_name;
    TextView song_album_name;
    TextView artist_name;
    TextView song_count;
    TextView timing_textview;
    TextView full_timing_textview;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    BottomSheetBehavior bottomSheetBehavior;

    ImageView audio_image;
    RelativeLayout no_audio_image_layout;
    TextView audio_title;
    TextView audio_authur;
    ImageView play_icon;
    ImageView queue_music;
    LinearLayout song_play_layout;
    CardView song_play_cardview;
    CardView audio_image_cardview;
    ViewPager pager;
    PopupWindow popupWindow;
    int i=0;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;



    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        sessionManager=new SessionManager(HomeScreen.this);
        background_of_layout=findViewById(R.id.background_of_layout);

        suggested_layout=findViewById(R.id.suggested_layout);
        songs_layout=findViewById(R.id.songs_layout);
        albums_layout=findViewById(R.id.albums_layout);
        artist_layout=findViewById(R.id.artist_layout);
        playlist_layout=findViewById(R.id.playlist_layout);
        folders_layout=findViewById(R.id.folders_layout);
        genres_layout=findViewById(R.id.genres_layout);
        other_option=findViewById(R.id.other_option);
//        song_count=findViewById(R.id.song_count);

        song_play_layout=findViewById(R.id.song_play_layout);
        song_play_cardview=findViewById(R.id.song_play_cardview);
        audio_image_cardview=findViewById(R.id.audio_image_cardview);
        audio_image=findViewById(R.id.audio_image);
        no_audio_image_layout=findViewById(R.id.no_audio_image_layout);
        audio_title=findViewById(R.id.audio_title);
        audio_authur=findViewById(R.id.audio_authur);
        play_icon=findViewById(R.id.play_icon);
        queue_music=findViewById(R.id.queue_music);


        horizontalScrollView=findViewById(R.id.horizontalScrollView);

        suggested_textview=findViewById(R.id.suggested_textview);
        songs_textview=findViewById(R.id.songs_textview);
        albums_textview=findViewById(R.id.albums_textview);
        artist_textview=findViewById(R.id.artist_textview);
        playlist_textview=findViewById(R.id.playlist_textview);
        folders_textview=findViewById(R.id.folders_textview);
        genres_textview=findViewById(R.id.genres_textview);
        search_navigation=findViewById(R.id.search_navigation);


        suggested_view=findViewById(R.id.suggested_view);
        songs_view=findViewById(R.id.songs_view);
        albums_view=findViewById(R.id.albums_view);
        artist_view=findViewById(R.id.artist_view);
        playlist_view=findViewById(R.id.playlist_view);
        folders_view=findViewById(R.id.folders_view);
        genres_view=findViewById(R.id.genres_view);
       // myReceiver = new MusicIntentReceiver();

//aji added playing page
        queue_music1=findViewById(R.id.queue_music1);
        play_button=findViewById(R.id.play_button);

        currentSongpage=findViewById(R.id.currentSongpage);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mShakeDetector = new ShakeDetector();

//        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
//            @Override
//            public void onShake() {
//                mSensorManager.unregisterListener(mShakeDetector);
//
//                System.out.println("aji shake");
//
//                if(BackgroundMusicService.bg_mediaPlayer!=null)
//                {
//
//                    if(sessionManager.getSongPosition()+1==SongPlayingActivity.audioModelList.size())
//                    {
//                        //sessionManager.setSongPosition(0);
//                        System.out.println("aji position equal to playing queue");
//                    }
//                    else {
//
//                            int next_song_position = sessionManager.getSongPosition();
//                            next_song_position = next_song_position + 1;
//                            sessionManager.setSongPosition(next_song_position);
//                        System.out.println("aji nxt position "+next_song_position);
//                            Gson gson = new Gson();
//                            String json = gson.toJson(SongPlayingActivity.audioModelList.get(sessionManager.getSongPosition()));
//                            sessionManager.setSong_Json(json);
//                            int current_song_count = sessionManager.getSongPosition() + 1;
//                            JSONObject jsonObject = null;
//                            try {
//                                jsonObject = new JSONObject(sessionManager.getSong_Json());
//                                sessionManager.setBackgroundMusic(jsonObject.optString("aPath"));
//
//
//                                System.out.println("aji shake playing path "+jsonObject);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Intent stopserviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
//                            stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
//                            stopService(stopserviceIntent);
//                            Intent serviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
//                            serviceIntent.putExtra("song_name", jsonObject.optString("track"));
//                            serviceIntent.putExtra("artist_name", jsonObject.optString("aArtist"));
//                            serviceIntent.putExtra("song_count", current_song_count + "/" + SongPlayingActivity.audioModelList.size());
//                            serviceIntent.putExtra("album_art", jsonObject.optString("aAlbumart"));
//                            serviceIntent.setAction(Constants.ACTION.NEXT_ACTION);
//                            startService(serviceIntent);
//                        try {
//                            jsonObject = new JSONObject(sessionManager.getSong_Json());
//                            playing_song_artist.setText(jsonObject.optString("aArtist"));
//                            playing_song_folder.setText(jsonObject.optString("aAlbum"));
//                            playing_song_name.setText(jsonObject.optString("track"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if(BackgroundMusicService.bg_mediaPlayer!=null)
//                        {
//                            System.out.println("aji bg notnull");
//                            if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
//                            {
//                                System.out.println("aji playing");
//                                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
//                            }
//                            else
//                            {
//                                System.out.println("aji bg notnull");
//                                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
//                            }
//                        }
//                        else
//                        {
//                            System.out.println("aji bg null");
//                            // Global.intent_method(PlaylistSongsDisplay.this, SongPlayingActivity.class);
//                        }
//
//
//                        }
//
////                    if (BackgroundMusicService.bg_mediaPlayer.isPlaying())
////                BackgroundMusicService.bg_mediaPlayer.pause();
////                    else
////                        BackgroundMusicService.bg_mediaPlayer.start();
//
//
//                }
////                mSensorManager.registerListener(mShakeDetector,
////                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
////                        SensorManager.SENSOR_DELAY_UI);
//
//            }
//        });

        playing_song_name=findViewById(R.id.playing_song_name);
        playing_song_folder=findViewById(R.id.playing_song_folder);
        playing_song_artist=findViewById(R.id.playing_song_artist);
        if(BackgroundMusicService.bg_mediaPlayer!=null)
        {
            System.out.println("aji bg notnull");
            if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
            {
                System.out.println("aji playing");
                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            }
            else
            {
                System.out.println("aji bg notnull");
                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            }
        }
        else
        {
            System.out.println("aji bg null");
            // Global.intent_method(PlaylistSongsDisplay.this, SongPlayingActivity.class);
        }
        try {
            jsonObject = new JSONObject(sessionManager.getSong_Json());
            playing_song_artist.setText(jsonObject.optString("aArtist"));
            playing_song_folder.setText(jsonObject.optString("aAlbum"));
            playing_song_name.setText(jsonObject.optString("track"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        LinearLayout toggle_drawer = (LinearLayout) findViewById(R.id.toggle_drawer);
        toggle_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        search_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, SearchActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


          pager = (ViewPager) findViewById(R.id.home_viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
//        SuggestedFragment suggestedFragment=new SuggestedFragment();
//        openFragment(suggestedFragment);

        queue_music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeScreen.this,PlayingQueue.class);
                intent.putExtra("title","Playing queue");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });



        currentSongpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   songPlayingActivity.on_song_play_UiChange("1");
                //Global.intent_method(HomeScreen.this, SongPlayingActivity.class);
                Intent intent=new Intent(HomeScreen.this,SongPlayingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("value",1);
                HomeScreen.this.startActivity(intent);
            }
        });



        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BackgroundMusicService.bg_mediaPlayer!=null)
                {
                    System.out.println("aji not null");
                    if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
                    {

                        play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    }
                    else
                    {
                        play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    }
                    Intent serviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
                    serviceIntent.setAction(Constants.ACTION.PLAY_ACTION);
                    startService(serviceIntent);
                }
                else
                {
                    play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    //Global.intent_method(HomeScreen.this, SongPlayingActivity.class);
                    Intent intent=new Intent(HomeScreen.this,SongPlayingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("value",2);
                    HomeScreen.this.startActivity(intent);
                }
            }
        });




        other_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(pager.getCurrentItem()==0)
                {
                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
                    PopupMenu popup = new PopupMenu(wrapper,other_option);
                    popup.getMenuInflater().inflate(R.menu.global_suggested_fragment_popupmenu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            System.out.println("aji 1");
                          switch(item.getItemId())
                          {
                              case R.id.shuffle_all:
                                  shuffle_all();
                                  break;

                              case R.id.settings:
                                  Global.intent_method(HomeScreen.this,Settings.class);
                                  break;
                              case R.id.equalizer:
                                  Global.intent_method(HomeScreen.this,EqualizerActivity.class);
                                  break;

                              case R.id.share_this_app:
                                  Intent intent = new Intent(Intent.ACTION_SEND);
                                  intent.putExtra(Intent.EXTRA_TEXT,
                                          "Hey, I Recommend this App for you. Its Most Stylish Mp3 Music Player For your Android Device, You would Definitely Like It. Please Try it Out. ");
                                  intent.setType("text/plain");
                                  startActivity(intent);
                                  break;
                          }
//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                            return true;
                        }
                    });

                    popup.show(); //sho
                }
                else if(pager.getCurrentItem()==1)
                {
                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
                    PopupMenu popup = new PopupMenu(wrapper,other_option);
                    popup.getMenuInflater().inflate(R.menu.global_song_fragment_popupmenu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick( MenuItem item) {
                            System.out.println("aji 22");
                            switch(item.getItemId())
                            {
                                case R.id.shuffle_all:
                                    shuffle_all();
                                    break;

                                case R.id.settings:
                                    Global.intent_method(HomeScreen.this,Settings.class);
                                    break;

                                case R.id.equalizer:
                                    Global.intent_method(HomeScreen.this,EqualizerActivity.class);
                                    break;

                                case R.id.share_this_app:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,
                                            "Hey, I Recommend this App for you. Its Most Stylish Mp3 Music Player For your Android Device, You would Definitely Like It. Please Try it Out. ");
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                    break;
                                case R.id.grid_size:
//                                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
//                                    PopupMenu popup = new PopupMenu(wrapper,other_option);
//                                    popup.getMenuInflater().inflate(R.menu.global_song_fragment_popupmenu, popup.getMenu());
//                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                        public boolean onMenuItemClick(MenuItem item) {
//                                            switch (item.getItemId())
//                                            {
//                                                default:
//                                            }
//                                            return true;
//
//                                        }
//                                        });
//                                    popup.show();
                                    LayoutInflater layoutInflater = (LayoutInflater) HomeScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View customView = layoutInflater.inflate(R.layout.grid_view_popup,null);

                                    grid_1 = customView.findViewById(R.id.grid_1);
                                    grid_2 = customView.findViewById(R.id.grid_2);
                                    grid_3 = customView.findViewById(R.id.grid_3);
                                    grid_4 = customView.findViewById(R.id.grid_4);

                                    //instantiate popup window
                                    popupWindow = new PopupWindow(customView, 300, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    //display the popup window
                                    popupWindow.showAtLocation(background_of_layout, Gravity.TOP|Gravity.END, 0, 40);
                                    grid_1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsGrid(1);
                                                  if (SongsFragment.songsAdapter!=null){
                                                      SongsFragment.song_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,1 , LinearLayoutManager.VERTICAL, false));
                                                      SongsFragment.song_recycler.setAdapter(SongsFragment.songsAdapter);
                                                      SongsFragment.songsAdapter.notifyDataSetChanged();
                                                  }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    grid_2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsGrid(2);
                                            if (SongsFragment.songsAdapter!=null){
                                                SongsFragment.song_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,2 , LinearLayoutManager.VERTICAL, false));
                                                SongsFragment.song_recycler.setAdapter(SongsFragment.songsAdapter);
                                                SongsFragment.songsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    grid_3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsGrid(3);
                                            if (SongsFragment.songsAdapter!=null){
                                                SongsFragment.song_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,3 , LinearLayoutManager.VERTICAL, false));
                                                SongsFragment.song_recycler.setAdapter(SongsFragment.songsAdapter);
                                                SongsFragment.songsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    grid_4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsGrid(4);
                                            if (SongsFragment.songsAdapter!=null){
                                                SongsFragment.song_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,4 , LinearLayoutManager.VERTICAL, false));
                                                SongsFragment.song_recycler.setAdapter(SongsFragment.songsAdapter);
                                                SongsFragment.songsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    break;


                                case R.id.sort_order:
//                                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
////                PopupMenu popup = new PopupMenu(wrapper, view);
//                                    PopupMenu popup1 = new PopupMenu(wrapper, other_option);
//                                    //Inflating the Popup using xml file
//                                    popup1.getMenuInflater().inflate(R.menu.sorting_menu, popup1.getMenu());
//                                    //registering popup with OnMenuItemClickListener
//                                    popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                        @Override
//                                        public boolean onMenuItemClick(MenuItem menuItem) {
//
//                                            switch (menuItem.getItemId()){
//                                                case R.id.ascending:
//                                                    System.out.println("aji size");
//
//                                                    List<AudioModel> audioModel_temp=SongsFragment.songsAdapter.audioModelList;
//                                                    System.out.println("aji size"+audioModel_temp.size());
////                                                    Collections.sort(audioModel_temp, new Comparator<AudioModel>(){
////                                                        public int compare(AudioModel o1, AudioModel o2)
////                                                        {
////                                                            return o2.getaName().compareTo(o1.getaName());
////                                                        }
////                                                    });
////                                                   for (int d=0;d<audioModel_temp.size();d++)
////                                                       System.out.println("aji "+audioModel_temp.get(d).getaName());
//
////                                                    songsAdapter.notifyDataSetChanged();
//                                                    Collections.sort(SongsFragment.songsAdapter.audioModelList,AudioModel.namecomparator);
//                                                    SongsFragment.songsAdapter.notifyDataSetChanged();
//                                                    break;
//                                                case R.id.descending:
//                                                    Collections.sort(SongsFragment.songsAdapter.audioModelList,
//                                                            AudioModel.descending);
////                                                    songsAdapter.notifyDataSetChanged();
//                                                    SongsFragment.songsAdapter.notifyDataSetChanged();
//                                                    break;
                                                //AudioModel.namecomparator(audioModelList,audioModelList.get(0).getaName());
//                                                default:
//                                            }
//                                            return true;
//                                        }
//                                    });

                               // popup1.show();


                                     layoutInflater = (LayoutInflater) HomeScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                     customView = layoutInflater.inflate(R.layout.sorting_popup_song,null);

                                    ascending = customView.findViewById(R.id.ascending);
                                    descending = customView.findViewById(R.id.descending);
                                    artist = customView.findViewById(R.id.artist);
                                    album = customView.findViewById(R.id.album);


                                    //instantiate popup window
                                    popupWindow = new PopupWindow(customView, 300, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    //display the popup window
                                    popupWindow.showAtLocation(background_of_layout, Gravity.TOP|Gravity.END, 0, 40);
                                    ascending.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsSort(1);
                                            Collections.sort(SongsFragment.songsAdapter.audioModelList,AudioModel.namecomparator);
                                            SongsFragment.songsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    descending.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsSort(2);
                                            Collections.sort(SongsFragment.songsAdapter.audioModelList,AudioModel.descending);
                                            SongsFragment.songsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    artist.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsSort(3);

                                            Collections.sort(SongsFragment.songsAdapter.audioModelList,AudioModel.artist);
                                            SongsFragment.songsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    album.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setSongsSort(4);

                                            Collections.sort(SongsFragment.songsAdapter.audioModelList,AudioModel.album);
                                            SongsFragment.songsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    break;
                            }




//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                            return true;
                        }
                    });

                    popup.show(); //sho
                }
                else if(pager.getCurrentItem()==2)
                {
                    //album ffragment
                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
                    PopupMenu popup = new PopupMenu(wrapper,other_option);
                    popup.getMenuInflater().inflate(R.menu.global_album_fragment_popupmenu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            System.out.println("aji 323");
                            switch(item.getItemId())
                            {
                                case R.id.shuffle_all:
                                    shuffle_all();
                                    break;

                                case R.id.settings:
                                    Global.intent_method(HomeScreen.this,Settings.class);
                                    break;

                                case R.id.equalizer:
                                    Global.intent_method(HomeScreen.this,EqualizerActivity.class);
                                    break;

                                case R.id.share_this_app:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,
                                            "Hey, I Recommend this App for you. Its Most Stylish Mp3 Music Player For your Android Device, You would Definitely Like It. Please Try it Out. ");
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                    break;



                                case R.id.grid_size:
                                    LayoutInflater layoutInflater = (LayoutInflater) HomeScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View customView = layoutInflater.inflate(R.layout.grid_view_popup,null);

                                    grid_1 = customView.findViewById(R.id.grid_1);
                                    grid_2 = customView.findViewById(R.id.grid_2);
                                    grid_3 = customView.findViewById(R.id.grid_3);
                                    grid_4 = customView.findViewById(R.id.grid_4);

                                    //instantiate popup window
                                    popupWindow = new PopupWindow(customView, 300, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    //display the popup window
                                    popupWindow.showAtLocation(background_of_layout, Gravity.TOP|Gravity.END, 0, 40);
                                    grid_1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsGrid(1);
                                            if (AlbumFragment.albumsAdapter !=null){
                                                AlbumFragment.albums_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,1 , LinearLayoutManager.VERTICAL, false));
                                                AlbumFragment.albums_recycler.setAdapter(AlbumFragment.albumsAdapter);
                                                AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    grid_2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsGrid(2);
                                            if (AlbumFragment.albumsAdapter !=null){
                                                AlbumFragment.albums_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,2 , LinearLayoutManager.VERTICAL, false));
                                                AlbumFragment.albums_recycler.setAdapter(AlbumFragment.albumsAdapter);
                                                AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    grid_3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsGrid(3);
                                            if (AlbumFragment.albumsAdapter !=null){
                                                AlbumFragment.albums_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,3 , LinearLayoutManager.VERTICAL, false));
                                                AlbumFragment.albums_recycler.setAdapter(AlbumFragment.albumsAdapter);
                                                AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    grid_4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsGrid(4);
                                            if (AlbumFragment.albumsAdapter !=null){
                                                AlbumFragment.albums_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,4 , LinearLayoutManager.VERTICAL, false));
                                                AlbumFragment.albums_recycler.setAdapter(AlbumFragment.albumsAdapter);
                                                AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    break;

                                case R.id.sort_order:
                                    layoutInflater = (LayoutInflater) HomeScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    customView = layoutInflater.inflate(R.layout.sorting_popup_album,null);

                                    ascending = customView.findViewById(R.id.ascending);
                                    descending = customView.findViewById(R.id.descending);
                                    artist = customView.findViewById(R.id.artist);
                                    year_sort = customView.findViewById(R.id.year_sort);


                                    //instantiate popup window
                                    popupWindow = new PopupWindow(customView, 300, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    //display the popup window
                                    popupWindow.showAtLocation(background_of_layout, Gravity.TOP|Gravity.END, 0, 40);
                                    ascending.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsSort(1);
                                            Collections.sort(AlbumFragment.albumsAdapter.albumModelList, AlbumModel.namecomparator);
                                            AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    descending.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsSort(2);
                                            Collections.sort(AlbumFragment.albumsAdapter.albumModelList, AlbumModel.descending);
                                            AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    artist.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsSort(3);

                                            Collections.sort(AlbumFragment.albumsAdapter.albumModelList, AlbumModel.artist);
                                            AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    year_sort.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setAlbumsSort(4);

                                            Collections.sort(AlbumFragment.albumsAdapter.albumModelList, AlbumModel.year_sort);
                                            AlbumFragment.albumsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });

                                    break;
                            }

//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                            return true;
                        }
                    });

                    popup.show(); //sho
                }
                else if(pager.getCurrentItem()==3)
                {
                    //Artist Fragment----------
                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
                    PopupMenu popup = new PopupMenu(wrapper,other_option);
                    popup.getMenuInflater().inflate(R.menu.global_artist_fragment_popupmenu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            System.out.println("aji 44");
                            switch(item.getItemId())
                            {
                                case R.id.shuffle_all:
                                    shuffle_all();
                                    break;

                                case R.id.settings:
                                    Global.intent_method(HomeScreen.this,Settings.class);
                                    break;

                                case R.id.equalizer:
                                    Global.intent_method(HomeScreen.this,EqualizerActivity.class);
                                    break;

                                case R.id.share_this_app:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,
                                            "Hey, I Recommend this App for you. Its Most Stylish Mp3 Music Player For your Android Device, You would Definitely Like It. Please Try it Out. ");
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                    break;
                                case R.id.grid_style:

                                    LayoutInflater layoutInflater = (LayoutInflater) HomeScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View customView = layoutInflater.inflate(R.layout.grid_style_popoup,null);

                                    circular_style = customView.findViewById(R.id.circular_style);
                                    default_style = customView.findViewById(R.id.default_style);
                                    //instantiate popup window
                                    popupWindow = new PopupWindow(customView, 300, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    //display the popup window
                                    popupWindow.showAtLocation(background_of_layout, Gravity.TOP|Gravity.END, 0, 40);
                                    default_style.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setArtistGridStyle(1);
                                            if (ArtistsFragment.artistsAdapter !=null){
                                                ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });
                                    circular_style.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setArtistGridStyle(2);
                                            if (ArtistsFragment.artistsAdapter !=null){
                                                ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                            }


                                            popupWindow.dismiss();
                                        }
                                    });

                                    break;

                                case R.id.grid_size:

                                 layoutInflater = (LayoutInflater) HomeScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                 customView = layoutInflater.inflate(R.layout.grid_view_popup,null);

                                grid_1 = customView.findViewById(R.id.grid_1);
                                grid_2 = customView.findViewById(R.id.grid_2);
                                grid_3 = customView.findViewById(R.id.grid_3);
                                grid_4 = customView.findViewById(R.id.grid_4);

                                //instantiate popup window
                                popupWindow = new PopupWindow(customView, 300, LinearLayout.LayoutParams.WRAP_CONTENT);

                                //display the popup window
                                popupWindow.showAtLocation(background_of_layout, Gravity.TOP|Gravity.END, 0, 40);
                                grid_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sessionManager.setArtistGrid(1);
                                        if (ArtistsFragment.artistsAdapter !=null){
                                            ArtistsFragment.artists_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,1 , LinearLayoutManager.VERTICAL, false));
                                            ArtistsFragment.artists_recycler.setAdapter(ArtistsFragment.artistsAdapter);
                                            ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                        }


                                        popupWindow.dismiss();
                                    }
                                });
                                grid_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sessionManager.setArtistGrid(2);
                                        if (ArtistsFragment.artistsAdapter !=null){
                                            ArtistsFragment.artists_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,2 , LinearLayoutManager.VERTICAL, false));
                                            ArtistsFragment.artists_recycler.setAdapter(ArtistsFragment.artistsAdapter);
                                            ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                        }


                                        popupWindow.dismiss();
                                    }
                                });
                                grid_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sessionManager.setArtistGrid(3);
                                        if (ArtistsFragment.artistsAdapter !=null){
                                            ArtistsFragment.artists_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,3 , LinearLayoutManager.VERTICAL, false));
                                            ArtistsFragment.artists_recycler.setAdapter(ArtistsFragment.artistsAdapter);
                                            ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                        }


                                        popupWindow.dismiss();
                                    }
                                });
                                grid_4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sessionManager.setArtistGrid(4);
                                        if (ArtistsFragment.artistsAdapter !=null){
                                            ArtistsFragment.artists_recycler.setLayoutManager(new GridLayoutManager(HomeScreen.this,4 , LinearLayoutManager.VERTICAL, false));
                                            ArtistsFragment.artists_recycler.setAdapter(ArtistsFragment.artistsAdapter);
                                            ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                        }


                                        popupWindow.dismiss();
                                    }
                                });
                                break;

                                case R.id.sort_order:
                                    layoutInflater = (LayoutInflater) HomeScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    customView = layoutInflater.inflate(R.layout.sorting_popup_artist,null);

                                    ascending = customView.findViewById(R.id.ascending);
                                    descending = customView.findViewById(R.id.descending);



                                    //instantiate popup window
                                    popupWindow = new PopupWindow(customView, 300, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    //display the popup window
                                    popupWindow.showAtLocation(background_of_layout, Gravity.TOP|Gravity.END, 0, 40);
                                    ascending.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setArtistSort(1);
                                            Collections.sort(ArtistsFragment.artistsAdapter.artistsModelList,ArtistsModel.namecomparator );
                                            ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    descending.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sessionManager.setArtistSort(2);
                                            Collections.sort(ArtistsFragment.artistsAdapter.artistsModelList, ArtistsModel.descending);
                                            ArtistsFragment.artistsAdapter.notifyDataSetChanged();
                                            popupWindow.dismiss();
                                        }
                                    });
                                    break;
                            }

//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();



                            return true;
                        }
                    });

                    popup.show(); //sho
                }
                else if(pager.getCurrentItem()==4)
                {
                    //playlist
                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
                    final PopupMenu popup = new PopupMenu(wrapper,other_option);
                    popup.getMenuInflater().inflate(R.menu.global_playlist_fragment_popupmenu, popup.getMenu());
                    if(sessionManager.getShowSmartPlaylist()){
                    popup.getMenu().findItem(R.id.show_smart_playlist).setTitle("Hide smart playlist");}
                    else{
                        popup.getMenu().findItem(R.id.show_smart_playlist).setTitle("Show smart playlist");}


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId())
                            {


                                case R.id.settings:
                                    Global.intent_method(HomeScreen.this,Settings.class);
                                    break;

                                case R.id.equalizer:
                                    Global.intent_method(HomeScreen.this,EqualizerActivity.class);
                                    break;

                                case R.id.share_this_app:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,
                                            "Hey, I Recommend this App for you. Its Most Stylish Mp3 Music Player For your Android Device, You would Definitely Like It. Please Try it Out. ");
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                    break;
                                case R.id.new_playlist:
                                    break;

                                    case R.id.show_smart_playlist:
                                        if(sessionManager.getShowSmartPlaylist())
                                        {
                                            sessionManager.setShowSmartPlaylist(false);
                                            PlaylistFragment.smart_list.setVisibility(View.GONE);
                                            popup.getMenu().findItem(R.id.show_smart_playlist).setTitle("Show smart playlist");
                                        }
                                        else
                                        {
                                            sessionManager.setShowSmartPlaylist(true);
                                            PlaylistFragment.smart_list.setVisibility(View.VISIBLE);
                                            popup.getMenu().findItem(R.id.show_smart_playlist).setTitle("Hide smart playlist");
                                        }
                                    break;

//                                case R.id.back_up_playlist:
//                                    break;
//
//                                    case R.id.restore_playlist:
//                                    break;
                            }
//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                            return true;
                        }
                    });

                    popup.show(); //sho
                }
                else if(pager.getCurrentItem()==5)
                {
                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
                    PopupMenu popup = new PopupMenu(wrapper,other_option);
                    popup.getMenuInflater().inflate(R.menu.global_folder_fragment_popupmenu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId())
                            {
                                case R.id.shuffle_all:
                                    shuffle_all();
                                    break;

                                case R.id.settings:
                                    Global.intent_method(HomeScreen.this,Settings.class);
                                    break;
                                case R.id.equalizer:
                                    Global.intent_method(HomeScreen.this,EqualizerActivity.class);
                                    break;

                                case R.id.share_this_app:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,
                                            "Hey, I Recommend this App for you. Its Most Stylish Mp3 Music Player For your Android Device, You would Definitely Like It. Please Try it Out. ");
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                    break;
                            }
//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                            return true;
                        }
                    });

                    popup.show(); //sho
                }
                else if(pager.getCurrentItem()==6)
                {
                    Context wrapper = new ContextThemeWrapper(HomeScreen.this, R.style.popupMenuStyle);
                    PopupMenu popup = new PopupMenu(wrapper,other_option);
                    popup.getMenuInflater().inflate(R.menu.global_folder_fragment_popupmenu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId())
                            {
                                case R.id.shuffle_all:

                                case R.id.settings:
                                    Global.intent_method(HomeScreen.this,Settings.class);
                                    break;
                                case R.id.equalizer:
                                    Global.intent_method(HomeScreen.this,EqualizerActivity.class);
                                    break;

                                case R.id.share_this_app:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,
                                            "Hey, I Recommend this App for you. Its Most Stylish Mp3 Music Player For your Android Device, You would Definitely Like It. Please Try it Out. ");
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                    break;
                            }
//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                            return true;
                        }
                    });

                    popup.show(); //sho
                }
            }
        });



        if(BackgroundMusicService.bg_mediaPlayer!=null)
        {
            if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
            {
                song_play_cardview.setVisibility(View.VISIBLE);
                if(sessionManager.getIsDefaultThemeSelected())
                {
//                    song_play_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));


                    Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette)
                        {
                            // access palette colors here
                            int vibrant = palette.getDominantColor(0x000000);
                            Log.e("abbas",""+vibrant);
                            song_play_layout.setBackgroundColor(vibrant);

                        }
                    };

                    Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(sessionManager.getSelectedTheme()));
                    if (myBitmap != null && !myBitmap.isRecycled()) {
                        Palette.from(myBitmap).generate(paletteListener);
                    }

                }
                else
                {
                    Drawable bg;
                    Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
                    try {

                        File f = new File(Global.getRealPathFromURI(HomeScreen.this,selectedImageUri));
                        bg= Drawable.createFromPath(f.getAbsolutePath());

                        Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette)
                            {
                                // access palette colors here
                                int vibrant = palette.getDominantColor(0x000000);
                                Log.e("abbas",""+vibrant);
                                song_play_layout.setBackgroundColor(vibrant);

                            }
                        };
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bmOptions);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            Palette.from(bitmap).generate(paletteListener);
                        }

//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
                    }
                    catch (Exception e)
                    {
                        bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
                        e.printStackTrace();
                    }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
//                    song_play_layout.setBackground(bg);
                }
                try
                {
                    JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
                    audio_authur.setText(jsonObject.optString("aAlbum")+"."+jsonObject.optString("aArtist"));
                    audio_title.setText(jsonObject.optString("track"));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                song_play_cardview.setVisibility(View.GONE);
            }
        }


        play_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BackgroundMusicService.bg_mediaPlayer!=null) {
                    if (BackgroundMusicService.bg_mediaPlayer.isPlaying()) {
                        play_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    } else {
                        play_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    }
                    Intent serviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
                    serviceIntent.setAction(Constants.ACTION.PLAY_ACTION);
                    startService(serviceIntent);
                }
            }
        });
        queue_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeScreen.this,PlayingQueue.class);
                intent.putExtra("title","Playing queue");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });





        suggested_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggested_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                suggested_view.setVisibility(View.VISIBLE);

                songs_textview.setTextColor(getResources().getColor(R.color.white));
                songs_view.setVisibility(View.INVISIBLE);

                albums_textview.setTextColor(getResources().getColor(R.color.white));
                albums_view.setVisibility(View.INVISIBLE);

                artist_textview.setTextColor(getResources().getColor(R.color.white));
                artist_view.setVisibility(View.INVISIBLE);

                playlist_textview.setTextColor(getResources().getColor(R.color.white));
                playlist_view.setVisibility(View.INVISIBLE);

                folders_textview.setTextColor(getResources().getColor(R.color.white));
                folders_view.setVisibility(View.INVISIBLE);

                genres_textview.setTextColor(getResources().getColor(R.color.white));
                genres_view.setVisibility(View.INVISIBLE);
                pager.setCurrentItem(0);
//                anim = ObjectAnimator.ofFloat(this, "motionX", 0, -pager.getWidth());
//                anim.setInterpolator(new LinearInterpolator());
//                anim.addListener(HomeScreen.this);
//                anim.setDuration(300);
//                anim.start();

                int x, y;
                x = suggested_layout.getLeft();
                y = suggested_layout.getTop();

                // Scroll to the button.
                horizontalScrollView.scrollTo(x, y);

//                SuggestedFragment suggestedFragment=new SuggestedFragment();
//                openFragment(suggestedFragment);

            }
        });

        songs_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggested_textview.setTextColor(getResources().getColor(R.color.white));
                suggested_view.setVisibility(View.INVISIBLE);

                songs_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                songs_view.setVisibility(View.VISIBLE);

                albums_textview.setTextColor(getResources().getColor(R.color.white));
                albums_view.setVisibility(View.INVISIBLE);

                artist_textview.setTextColor(getResources().getColor(R.color.white));
                artist_view.setVisibility(View.INVISIBLE);

                playlist_textview.setTextColor(getResources().getColor(R.color.white));
                playlist_view.setVisibility(View.INVISIBLE);

                folders_textview.setTextColor(getResources().getColor(R.color.white));
                folders_view.setVisibility(View.INVISIBLE);

                genres_textview.setTextColor(getResources().getColor(R.color.white));
                genres_view.setVisibility(View.INVISIBLE);
                pager.setCurrentItem(1);
//                anim = ObjectAnimator.ofFloat(this, "motionX", 0, -pager.getWidth());
//                anim.setInterpolator(new LinearInterpolator());
//                anim.addListener(HomeScreen.this);
//                anim.setDuration(300);
//                anim.start();
                int x, y;
                x = songs_layout.getLeft();
                y = songs_layout.getTop();

                // Scroll to the button.
                horizontalScrollView.scrollTo(x, y);


//                SongsFragment songsFragment=new SongsFragment();
//                openFragment(songsFragment);
            }
        });

        albums_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggested_textview.setTextColor(getResources().getColor(R.color.white));
                suggested_view.setVisibility(View.INVISIBLE);

                songs_textview.setTextColor(getResources().getColor(R.color.white));
                songs_view.setVisibility(View.INVISIBLE);

                albums_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                albums_view.setVisibility(View.VISIBLE);

                artist_textview.setTextColor(getResources().getColor(R.color.white));
                artist_view.setVisibility(View.INVISIBLE);

                playlist_textview.setTextColor(getResources().getColor(R.color.white));
                playlist_view.setVisibility(View.INVISIBLE);

                folders_textview.setTextColor(getResources().getColor(R.color.white));
                folders_view.setVisibility(View.INVISIBLE);

                genres_textview.setTextColor(getResources().getColor(R.color.white));
                genres_view.setVisibility(View.INVISIBLE);

                pager.setCurrentItem(2);
//                anim = ObjectAnimator.ofFloat(this, "motionX", 0, -pager.getWidth());
//                anim.setInterpolator(new LinearInterpolator());
//                anim.addListener(HomeScreen.this);
//                anim.setDuration(300);
//                anim.start();


                int x, y;
                x = albums_layout.getLeft();
                y = albums_layout.getTop();
                horizontalScrollView.scrollTo(x, y);
//                AlbumFragment albumFragment=new AlbumFragment();
//                openFragment(albumFragment);
            }
        });

        artist_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggested_textview.setTextColor(getResources().getColor(R.color.white));
                suggested_view.setVisibility(View.INVISIBLE);

                songs_textview.setTextColor(getResources().getColor(R.color.white));
                songs_view.setVisibility(View.INVISIBLE);

                albums_textview.setTextColor(getResources().getColor(R.color.white));
                albums_view.setVisibility(View.INVISIBLE);

                artist_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                artist_view.setVisibility(View.VISIBLE);

                playlist_textview.setTextColor(getResources().getColor(R.color.white));
                playlist_view.setVisibility(View.INVISIBLE);

                folders_textview.setTextColor(getResources().getColor(R.color.white));
                folders_view.setVisibility(View.INVISIBLE);

                genres_textview.setTextColor(getResources().getColor(R.color.white));
                genres_view.setVisibility(View.INVISIBLE);

                pager.setCurrentItem(3);
//                anim = ObjectAnimator.ofFloat(this, "motionX", 0, -pager.getWidth());
//                anim.setInterpolator(new LinearInterpolator());
//                anim.addListener(HomeScreen.this);
//                anim.setDuration(300);
//                anim.start();

                int x, y;
                x = artist_layout.getLeft();
                y = artist_layout.getTop();
                horizontalScrollView.scrollTo(x, y);
//                ArtistsFragment artistsFragment=new ArtistsFragment();
//                openFragment(artistsFragment);
            }
        });

        playlist_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggested_textview.setTextColor(getResources().getColor(R.color.white));
                suggested_view.setVisibility(View.INVISIBLE);

                songs_textview.setTextColor(getResources().getColor(R.color.white));
                songs_view.setVisibility(View.INVISIBLE);

                albums_textview.setTextColor(getResources().getColor(R.color.white));
                albums_view.setVisibility(View.INVISIBLE);

                artist_textview.setTextColor(getResources().getColor(R.color.white));
                artist_view.setVisibility(View.INVISIBLE);

                playlist_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                playlist_view.setVisibility(View.VISIBLE);

                folders_textview.setTextColor(getResources().getColor(R.color.white));
                folders_view.setVisibility(View.INVISIBLE);

                genres_textview.setTextColor(getResources().getColor(R.color.white));
                genres_view.setVisibility(View.INVISIBLE);

                pager.setCurrentItem(4);
//                anim = ObjectAnimator.ofFloat(this, "motionX", 0, -pager.getWidth());
//                anim.setInterpolator(new LinearInterpolator());
//                anim.addListener(HomeScreen.this);
//                anim.setDuration(300);
//                anim.start();


                int x, y;
                x = playlist_layout.getLeft();
                y = playlist_layout.getTop();
                horizontalScrollView.scrollTo(x, y);

//                PlaylistFragment playlistFragment=new PlaylistFragment();
//                openFragment(playlistFragment);
            }
        });

        folders_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggested_textview.setTextColor(getResources().getColor(R.color.white));
                suggested_view.setVisibility(View.INVISIBLE);

                songs_textview.setTextColor(getResources().getColor(R.color.white));
                songs_view.setVisibility(View.INVISIBLE);

                albums_textview.setTextColor(getResources().getColor(R.color.white));
                albums_view.setVisibility(View.INVISIBLE);

                artist_textview.setTextColor(getResources().getColor(R.color.white));
                artist_view.setVisibility(View.INVISIBLE);

                playlist_textview.setTextColor(getResources().getColor(R.color.white));
                playlist_view.setVisibility(View.INVISIBLE);

                folders_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                folders_view.setVisibility(View.VISIBLE);

                genres_textview.setTextColor(getResources().getColor(R.color.white));
                genres_view.setVisibility(View.INVISIBLE);

                pager.setCurrentItem(5);
//                anim = ObjectAnimator.ofFloat(this, "motionX", 0, -pager.getWidth());
//                anim.setInterpolator(new LinearInterpolator());
//                anim.addListener(HomeScreen.this);
//                anim.setDuration(300);
//                anim.start();

//                FoldersFragment foldersFragment=new FoldersFragment();
//                openFragment(foldersFragment);
            }
        });

        genres_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggested_textview.setTextColor(getResources().getColor(R.color.white));
                suggested_view.setVisibility(View.INVISIBLE);

                songs_textview.setTextColor(getResources().getColor(R.color.white));
                songs_view.setVisibility(View.INVISIBLE);

                albums_textview.setTextColor(getResources().getColor(R.color.white));
                albums_view.setVisibility(View.INVISIBLE);

                artist_textview.setTextColor(getResources().getColor(R.color.white));
                artist_view.setVisibility(View.INVISIBLE);

                playlist_textview.setTextColor(getResources().getColor(R.color.white));
                playlist_view.setVisibility(View.INVISIBLE);

                folders_textview.setTextColor(getResources().getColor(R.color.white));
                folders_view.setVisibility(View.INVISIBLE);

                genres_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                genres_view.setVisibility(View.VISIBLE);

                pager.setCurrentItem(6);
//                anim = ObjectAnimator.ofFloat(this, "motionX", 0, -pager.getWidth());
//                anim.setInterpolator(new LinearInterpolator());
//                anim.addListener(HomeScreen.this);
//                anim.setDuration(300);
//                anim.start();

//                GenresFragment genresFragment=new GenresFragment();
//                openFragment(genresFragment);
            }
        });
//        pager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//
//        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                pager.getParent().requestDisallowInterceptTouchEvent(true);
//            }
//        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                Log.e("abbas","position "+position);
                switch(position) {

                    case 0:
                        suggested_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        suggested_view.setVisibility(View.VISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.white));
                        songs_view.setVisibility(View.INVISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.white));
                        albums_view.setVisibility(View.INVISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.white));
                        artist_view.setVisibility(View.INVISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.white));
                        playlist_view.setVisibility(View.INVISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.white));
                        folders_view.setVisibility(View.INVISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.white));
                        genres_view.setVisibility(View.INVISIBLE);

                        int x, y;
                        x = suggested_layout.getLeft();
                        y = suggested_layout.getTop();
                        horizontalScrollView.scrollTo(x, y);

                        break;
                    case 1:
                        suggested_textview.setTextColor(getResources().getColor(R.color.white));
                        suggested_view.setVisibility(View.INVISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        songs_view.setVisibility(View.VISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.white));
                        albums_view.setVisibility(View.INVISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.white));
                        artist_view.setVisibility(View.INVISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.white));
                        playlist_view.setVisibility(View.INVISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.white));
                        folders_view.setVisibility(View.INVISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.white));
                        genres_view.setVisibility(View.INVISIBLE);


                        int x1, y1;
                        x1 = songs_layout.getLeft();
                        y1 = songs_layout.getTop();
                        horizontalScrollView.scrollTo(x1, y1);

                        break;
                    case 2:
                        suggested_textview.setTextColor(getResources().getColor(R.color.white));
                        suggested_view.setVisibility(View.INVISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.white));
                        songs_view.setVisibility(View.INVISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        albums_view.setVisibility(View.VISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.white));
                        artist_view.setVisibility(View.INVISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.white));
                        playlist_view.setVisibility(View.INVISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.white));
                        folders_view.setVisibility(View.INVISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.white));
                        genres_view.setVisibility(View.INVISIBLE);

                        int x2, y2;
                        x2 = albums_layout.getLeft();
                        y2 = albums_layout.getTop();
                        horizontalScrollView.scrollTo(x2, y2);

                        break;
                    case 3:
                        suggested_textview.setTextColor(getResources().getColor(R.color.white));
                        suggested_view.setVisibility(View.INVISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.white));
                        songs_view.setVisibility(View.INVISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.white));
                        albums_view.setVisibility(View.INVISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        artist_view.setVisibility(View.VISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.white));
                        playlist_view.setVisibility(View.INVISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.white));
                        folders_view.setVisibility(View.INVISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.white));
                        genres_view.setVisibility(View.INVISIBLE);



                        int x3, y3;
                        x3 = artist_layout.getLeft();
                        y3 = artist_layout.getTop();
                        horizontalScrollView.scrollTo(x3, y3);


                        break;
                    case 4:
                        suggested_textview.setTextColor(getResources().getColor(R.color.white));
                        suggested_view.setVisibility(View.INVISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.white));
                        songs_view.setVisibility(View.INVISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.white));
                        albums_view.setVisibility(View.INVISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.white));
                        artist_view.setVisibility(View.INVISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        playlist_view.setVisibility(View.VISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.white));
                        folders_view.setVisibility(View.INVISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.white));
                        genres_view.setVisibility(View.INVISIBLE);


                        int x4, y4;
                        x4 = playlist_layout.getLeft();
                        y4 = playlist_layout.getTop();
                        horizontalScrollView.scrollTo(x4, y4);


                        break;
                    case 5:
                        suggested_textview.setTextColor(getResources().getColor(R.color.white));
                        suggested_view.setVisibility(View.INVISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.white));
                        songs_view.setVisibility(View.INVISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.white));
                        albums_view.setVisibility(View.INVISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.white));
                        artist_view.setVisibility(View.INVISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.white));
                        playlist_view.setVisibility(View.INVISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        folders_view.setVisibility(View.VISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.white));
                        genres_view.setVisibility(View.INVISIBLE);


//                        int x5, y5;
//                        x5 = folders_layout.getLeft();
//                        y5 = folders_layout.getTop();
//                        horizontalScrollView.scrollTo(x5, y5);

                        break;
                    case 6:
                        suggested_textview.setTextColor(getResources().getColor(R.color.white));
                        suggested_view.setVisibility(View.INVISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.white));
                        songs_view.setVisibility(View.INVISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.white));
                        albums_view.setVisibility(View.INVISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.white));
                        artist_view.setVisibility(View.INVISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.white));
                        playlist_view.setVisibility(View.INVISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.white));
                        folders_view.setVisibility(View.INVISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        genres_view.setVisibility(View.VISIBLE);



//                        int x6, y6;
//                        x6 = genres_layout.getLeft();
//                        y6 = genres_layout.getTop();
//                        horizontalScrollView.scrollTo(x6, y6);

                        break;
                    default:
                        suggested_textview.setTextColor(getResources().getColor(R.color.colorAccent));
                        suggested_view.setVisibility(View.VISIBLE);

                        songs_textview.setTextColor(getResources().getColor(R.color.white));
                        songs_view.setVisibility(View.INVISIBLE);

                        albums_textview.setTextColor(getResources().getColor(R.color.white));
                        albums_view.setVisibility(View.INVISIBLE);

                        artist_textview.setTextColor(getResources().getColor(R.color.white));
                        artist_view.setVisibility(View.INVISIBLE);

                        playlist_textview.setTextColor(getResources().getColor(R.color.white));
                        playlist_view.setVisibility(View.INVISIBLE);

                        folders_textview.setTextColor(getResources().getColor(R.color.white));
                        folders_view.setVisibility(View.INVISIBLE);

                        genres_textview.setTextColor(getResources().getColor(R.color.white));
                        genres_view.setVisibility(View.INVISIBLE);


//                        int x6, y6;
//                        x = suggested_layout.getLeft();
//                        y = suggested_layout.getTop();
//                        horizontalScrollView.scrollTo(x, y);

                        break;
                }
            }
        });

        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
            navigationView.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(HomeScreen.this,selectedImageUri));
                bg= Drawable.createFromPath(f.getAbsolutePath());


//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
                e.printStackTrace();
            }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
            background_of_layout.setBackground(bg);
            navigationView.setBackground(bg);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onResume(){
//        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(myReceiver, filter);
        super.onResume();
        if (PlaylistFragment.playlistAdapter!=null){
        PlaylistFragment.playlistAdapter.notifyDataSetChanged();}
//        i=0;
//        final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
//        System.out.println("aji type "+type);
//        audioModelList1 = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);

//        mSensorManager.registerListener(mShakeDetector,
//                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_UI);
        if(BackgroundMusicService.bg_mediaPlayer!=null)
        {
            System.out.println("aji bg notnull");
            if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
            {
                System.out.println("aji playing");
                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            }
            else
            {
                System.out.println("aji bg notnull");
                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            }
        }
        else
        {
            System.out.println("aji bg null");
            // Global.intent_method(PlaylistSongsDisplay.this, SongPlayingActivity.class);
        }
        try {
            jsonObject = new JSONObject(sessionManager.getSong_Json());
            playing_song_artist.setText(jsonObject.optString("aArtist"));
            playing_song_folder.setText(jsonObject.optString("aAlbum"));
            playing_song_name.setText(jsonObject.optString("track"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(BackgroundMusicService.bg_mediaPlayer!=null)
        {
            if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
            {
                song_play_cardview.setVisibility(View.VISIBLE);

                if(sessionManager.getIsDefaultThemeSelected())
                {
//                    song_play_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
                    Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette)
                        {
                            // access palette colors here
                            int vibrant = palette.getDominantColor(0x000000);
                            Log.e("abbas",""+vibrant);
                            song_play_layout.setBackgroundColor(vibrant);

                        }
                    };

                    Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(sessionManager.getSelectedTheme()));
                    if (myBitmap != null && !myBitmap.isRecycled())
                    {
                        Palette.from(myBitmap).generate(paletteListener);
                    }
                }
                else
                {
                    Drawable bg;
                    Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
                    try {

                        File f = new File(Global.getRealPathFromURI(HomeScreen.this,selectedImageUri));
                        bg= Drawable.createFromPath(f.getAbsolutePath());
                        Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette)
                            {
                                // access palette colors here
                                int vibrant = palette.getDominantColor(0x000000);
                                Log.e("abbas",""+vibrant);
                                song_play_layout.setBackgroundColor(vibrant);

                            }
                        };
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bmOptions);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            Palette.from(bitmap).generate(paletteListener);
                        }

//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
                    }
                    catch (Exception e)
                    {
                        bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
                        e.printStackTrace();
                    }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
//                    song_play_layout.setBackground(bg);
                }
                try
                {
                    JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
                    audio_authur.setText(jsonObject.optString("aAlbum")+" . "+jsonObject.optString("aArtist"));
                    audio_title.setText(jsonObject.optString("track"));
                    String album_art=jsonObject.optString("aAlbumart");
                    if(album_art==null || album_art.equals(""))
                    {
                        audio_image_cardview.setVisibility(View.GONE);
                        no_audio_image_layout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        no_audio_image_layout.setVisibility(View.GONE);
                        audio_image_cardview.setVisibility(View.VISIBLE);
                        Bitmap bm= BitmapFactory.decodeFile(album_art);
                        audio_image.setImageBitmap(bm);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                song_play_cardview.setVisibility(View.GONE);
            }
        }

    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.languages
        int id = item.getItemId();

        if (id == R.id.remove_ads) {


        }
        else if (id == R.id.themes) {

            Intent i = new Intent(HomeScreen.this, ThemeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.ringtone_cutter)
        {
            Intent i = new Intent(HomeScreen.this, RCSongSelectionActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
        else if (id == R.id.sleep_timer)
        {

            timerLayoutBinding=  TimerLayoutBinding.inflate(getLayoutInflater());
            View alertLayout = timerLayoutBinding.getRoot();

            if(sessionManager.getIsDefaultThemeSelected())
            {
                timerLayoutBinding.timerAlertBackground.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
            }
            else
            {
                Drawable bg;
                Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
                try {

                    File f = new File(Global.getRealPathFromURI(HomeScreen.this,selectedImageUri));
                    bg= Drawable.createFromPath(f.getAbsolutePath());

                }
                catch (Exception e)
                {
                    bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
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

            AlertDialog.Builder alert = new AlertDialog.Builder(HomeScreen.this);
            alert.setView(alertLayout);
//                alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timerLayoutBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    dialog.dismiss();
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
                    startTimer(timerLayoutBinding.progressSeekBar.getProgress(),dialog);
                    dialog.dismiss();
                }
            });
        }
        else if (id == R.id.equalizer) {
            Intent i = new Intent(HomeScreen.this, EqualizerActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
//        else if (id == R.id.drive_mode)
//        {
//            bottom_layout = new BottomSheetDialog(HomeScreen.this);
//            bottom_layout.setCancelable(false);
//            View parentView = getLayoutInflater().inflate(R.layout.drive_mode_layout,null);
//            close_play_button=parentView.findViewById(R.id.close_play_button);
//            previous_music=parentView.findViewById(R.id.previous_music);
//            next_music=parentView.findViewById(R.id.next_music);
//            final LinearLayout play_music =parentView.findViewById(R.id.play_music);
//            no_audio_image =parentView.findViewById(R.id.no_audio_image);
//            play_song_image=parentView.findViewById(R.id.play_song_image);
//            seekBar=parentView.findViewById(R.id.seekBar);
//            song_name=parentView.findViewById(R.id.song_name);
//            song_album_name=parentView.findViewById(R.id.song_album_name);
//            artist_name=parentView.findViewById(R.id.artist_name);
//            song_count=parentView.findViewById(R.id.song_count);
//            timing_textview=parentView.findViewById(R.id.timing_textview);
//            full_timing_textview=parentView.findViewById(R.id.full_timing_textview);
//            song_image=parentView.findViewById(R.id.song_image);
//            bottom_layout.setContentView(parentView);
//            bottom_layout.show();
//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            bottom_layout.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//            Realm.init(HomeScreen.this);
//            RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
//            music_database = Realm.getInstance(main_data_config);
//
//
//            final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
//            audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
//
//            play_music();
//            if(mPlayer.isPlaying())
//            {
//                play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
//            }
//            else
//            {
//                play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
//            }
//            close_play_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    bottom_layout.dismiss();
//                }
//            });
//            previous_music.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view)
//                {
//                    play_previous_song();
//
//                }
//            });
//            next_music.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    init=true;
//                    play_next_song();
//                }
//            });
//
//            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    int h = Math.round(progress);
//                    //In the above line we are converting the float value into int because
//// media player required int value and seekbar library gives progress in float
//                    if (mPlayer != null && fromUser) {
//                        mPlayer.seekTo(h);
//                    }
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
//            play_music.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(mPlayer.isPlaying())
//                    {
//                        init=false;
//                        mPlayer.pause();
//                        play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
//                    }
//                    else
//                    {
//                        init=false;
//                        mPlayer.start();
//                        play_song_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
//                    }
//                }
//            });
//
//        }
        else if (id == R.id.drive_mode)
        {
            Intent i = new Intent(HomeScreen.this, DriveModeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
        else if (id == R.id.hidden_folders) {

        }

        else if (id == R.id.scan_media) {
            Intent i = new Intent(HomeScreen.this, ScanMediaActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.settings) {
            Intent i = new Intent(HomeScreen.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.faq) {

        } else if (id == R.id.feedback) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.feedback_alert, null);
            final EditText feedback = alertLayout.findViewById(R.id.feedback);
            final TextView cancel = alertLayout.findViewById(R.id.cancel);
            final TextView submit = alertLayout.findViewById(R.id.submit);
            final LinearLayout feedback_layout = alertLayout.findViewById(R.id.feedback_layout);
            if(sessionManager.getIsDefaultThemeSelected())
            {
                feedback_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
            }
            else
            {
                Drawable bg;
                Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
                try {
                    File f = new File(Global.getRealPathFromURI(HomeScreen.this,selectedImageUri));
                    bg= Drawable.createFromPath(f.getAbsolutePath());
                }
                catch (Exception e)
                {
                    bg = ContextCompat.getDrawable(HomeScreen.this, R.drawable.theme_1);
                    e.printStackTrace();
                }
                feedback_layout.setBackground(bg);
            }
//            final EditText input = new EditText(HomeScreen.this);
//            input.setInputType(InputType.TYPE_CLASS_NUMBER);
//            input.setGravity(Gravity.CENTER);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT);
//            input.setLayoutParams(lp);

//            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//            sendIntent.setData(Uri.parse("test@gmail.com"));
//            sendIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
//            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "test@gmail.com" });
//            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "Test");
//            startActivity(sendIntent);
           // sendEmail(this,"smarthersm48@gmail.com","hello","");
            androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(HomeScreen.this);
            alert.setCancelable(false);
            alert.setView(alertLayout);
            final androidx.appcompat.app.AlertDialog dialog = alert.create();
            dialog.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "your_emailid@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                        intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                        startActivity(intent);
                    } catch(Exception e) {
                        Toast.makeText(HomeScreen.this, "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
//            alert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
////                    partner_district.setText(DropDownTable.call_required_listdata("8").get(which));
//                    scan(12,which);
//                }
//            });
//            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
////                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
////                            "mailto", "smarthersm48@gmail.com", null));
////                    emailIntent.setType("text/plain");
////
////                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
////                    emailIntent.putExtra(Intent.EXTRA_TEXT, feedback.getText().toString());
////
////                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
//
////                    Intent intent = new Intent(Intent.ACTION_SEND);
////                    intent.setType("message/rfc822");
////                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "hello@gmail.com" });
////                    intent.putExtra(Intent.EXTRA_SUBJECT, "any");
////                    intent.putExtra(Intent.EXTRA_TEXT, "any text");
////
////                        startActivity(intent);
////                        startActivity(Intent.createChooser(intent, "Send email..."));
//
//
//                }
//            });
//            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            //alert.show();



        } else if (id == R.id.info) {

        }
        else if (id == R.id.quit) {
                finishAffinity();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void startTimer(int minutes, final AlertDialog dialog)
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
                Intent stopserviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
                stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                stopService(stopserviceIntent);
            }
        };
        timer.start();
    }
    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
//            timing_textview.setText(String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(sTime),TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );

            long minutes = (mPlayer.getCurrentPosition() / 1000) / 60;
            long seconds = (mPlayer.getCurrentPosition() / 1000) % 60;
            String minutes_string=String.valueOf(minutes);
            String seconds_string=String.valueOf(seconds);

            long song_minutes = (mPlayer.getDuration() / 1000) / 60;
            long song_seconds = (mPlayer.getDuration()/ 1000) % 60;
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
            if(mPlayer.getCurrentPosition()==mPlayer.getDuration())
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

    public void play_music()
    {
        try {
            int current_song_count=sessionManager.getSongPosition()+1;
//            song_count.setText(current_song_count+"/"+audioModelList.size());
            JSONObject jsonObject=new JSONObject(sessionManager.getSong_Json());
            song_album_name.setText(jsonObject.optString("aAlbum"));
            song_name.setText(jsonObject.optString("track"));
            artist_name.setText(jsonObject.optString("aArtist"));
//            timing_textview.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
//                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
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
//            Glide.with(context).load().into(audio_image);
            }
            sessionManager.setBackgroundMusic(jsonObject.optString("aPath"));
//                startService(new Intent(SongPlayingActivity.this, BackgroundMusicService.class));

            Intent serviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
            serviceIntent.putExtra("song_name",jsonObject.optString("track"));
            serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
            serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
            serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(serviceIntent);



            Uri song_uri= Uri.fromFile(new File(jsonObject.optString("aPath")));
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(getApplicationContext(), song_uri);
            mPlayer.prepare();
            mPlayer.start();
            seekBar.setMax(mPlayer.getDuration());
            seekBar.setProgress(mPlayer.getCurrentPosition());

            eTime = mPlayer.getDuration();
            sTime = mPlayer.getCurrentPosition();
            if(oTime == 0){
//                songPrgs.setMax(eTime);
                seekBar.setMax(eTime);
                oTime =1;
            }
//            songTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime), TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
//            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime), TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
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


            seekBar.setProgress(sTime);
            hdlr.postDelayed(UpdateSongTime, 100);

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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



        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play_next_song()
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
            mPlayer.stop();
            play_music();
        }

    }
    public void play_previous_song()
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
            mPlayer.stop();
            play_music();
        }
    }
    private void animatePagerTransition(final boolean forward) {

        ValueAnimator animator = ValueAnimator.ofInt(0, pager.getWidth());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pager.endFakeDrag();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                pager.endFakeDrag();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private int oldDragPosition = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int dragPosition = (Integer) animation.getAnimatedValue();
                int dragOffset = dragPosition - oldDragPosition;
                oldDragPosition = dragPosition;
                pager.fakeDragBy(dragOffset * (forward ? -1 : 1));
            }
        });

        animator.setDuration(15000);
        if (pager.beginFakeDrag()) {
            animator.start();
        }
    }

    @Override
    public void onAnimationStart(Animator animator) {
        mLastMotionX = 0;
        mIsInAnimation = true;
        final long time = SystemClock.uptimeMillis();
        simulate(MotionEvent.ACTION_DOWN, time, time);
        mMotionBeginTime = time;
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        mIsInAnimation = false;
        final long time = SystemClock.uptimeMillis();
        simulate(MotionEvent.ACTION_UP, mMotionBeginTime, time);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    private void simulate(int action, long startTime, long endTime) {
        // specify the property for the two touch points
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[1];
        MotionEvent.PointerProperties pp = new MotionEvent.PointerProperties();
        pp.id = 0;
        pp.toolType = MotionEvent.TOOL_TYPE_FINGER;

        properties[0] = pp;

        // specify the coordinations of the two touch points
        // NOTE: you MUST set the pressure and size value, or it doesn't work
        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[1];
        MotionEvent.PointerCoords pc = new MotionEvent.PointerCoords();
        pc.x = mLastMotionX;
        pc.pressure = 1;
        pc.size = 1;
        pointerCoords[0] = pc;

        final MotionEvent ev = MotionEvent.obtain(
                startTime, endTime, action, 1, properties,
                pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0);

        pager.dispatchTouchEvent(ev);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeScreen.this);
            alertDialogBuilder.setTitle("Exit App");
            alertDialogBuilder
                    .setMessage("Do you want to exit app?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(a);
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }
    public Intent sendEmail(Context context, String email, String subject, String body){

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, email); // if you wanted to send multiple emails then you would use intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email })
        intent.putExtra(Intent.EXTRA_TEXT, body); // optional if you have the body in your mailto: link
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); // optional if you have the subject in your mailto: link
        intent.setType("text/plain");

        return intent;
    }
    public void shuffle_all(){
        final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
         audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
        System.out.println("aji before shuffle "+audioModelList.get(0).getaName());
//        Random random = new Random();
        if(audioModelList.size()!=0) {
            Collections.shuffle(audioModelList);
            System.out.println("aji after shuffle "+audioModelList.get(0).getaName());
            sessionManager.setSongPosition(0);
            Gson gson = new Gson();
            int current_song_count=sessionManager.getSongPosition()+1;
            String json = gson.toJson(audioModelList.get(sessionManager.getSongPosition()));
            sessionManager.setSong_Json(json);
            try {
                jsonObject = new JSONObject(sessionManager.getSong_Json());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            playing_song_artist.setText(jsonObject.optString("aArtist"));
            playing_song_folder.setText(jsonObject.optString("aAlbum"));
            playing_song_name.setText(jsonObject.optString("track"));

            Gson gson_lists = new GsonBuilder().create();
            JsonArray song_arrays = gson_lists.toJsonTree(audioModelList).getAsJsonArray();
            sessionManager.setSong_JsonList(song_arrays.toString());
            sessionManager.setBackgroundMusic(jsonObject.optString("aPath"));
            System.out.println("aji home shuffle "+jsonObject.optString("aPath"));
            System.out.println("aji home name "+jsonObject.optString("aName"));
//                startService(new Intent(SongPlayingActivity.this, BackgroundMusicService.class));
            Intent stopserviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
            stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            stopService(stopserviceIntent);
            Intent serviceIntent = new Intent(HomeScreen.this, BackgroundMusicService.class);
            serviceIntent.putExtra("song_name",jsonObject.optString("track"));
            serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
            serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
            serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(serviceIntent);

        }
    }

    @Override public void onPause() {
       // unregisterReceiver(myReceiver);
        super.onPause();
    }
}