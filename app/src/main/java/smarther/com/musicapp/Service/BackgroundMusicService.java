package smarther.com.musicapp.Service;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.palette.graphics.Palette;
import smarther.com.musicapp.Activity.HomeScreen;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Activity.SplashActivity;
import smarther.com.musicapp.Activity.WelcomeActivity;
import smarther.com.musicapp.Model.EqualizerSeekbarModel;
import smarther.com.musicapp.Model.MediaPlayerModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Constants;
import smarther.com.musicapp.Utils.EqualizerSettings;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.NotificationReceiver;
import smarther.com.musicapp.Utils.SessionManager;
import smarther.com.musicapp.Utils.ShakeDetector;

import static smarther.com.musicapp.Application.MyApplication.CHANNEL_ID;

public class BackgroundMusicService extends Service {

  public static   MediaPlayer bg_mediaPlayer;
    public static SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public static ShakeDetector mShakeDetector;

    SessionManager sessionManager;
    public static Equalizer mequalizer;
    public static Virtualizer mVirtualizer;
    public static BassBoost mBassBoost;
    public static PresetReverb mReverb;
    public AudioManager audioManager;
   String song_name="";
   String artist_name="";
   String song_count="";
   String album_art="";
   String play_mode="";
    MusicIntentReceiver myReceiver;
    RemoteViews collapsedView;
    RemoteViews expandedView;
   // public MusicIntentReceiver myReceiver;
   Uri song_uri;
    private Handler hdlr = new Handler();

    private NotificationManagerCompat notificationManager;
//    Notification notification ;
    NotificationCompat.Builder notificationBuilder;
    PendingIntent pendingIntent;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    public void onCreate()
    {

        myReceiver = new MusicIntentReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        notificationManager = NotificationManagerCompat.from(this);

        Log.e("aji","Service Created");
        System.out.println("aji background service created");


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mShakeDetector = new ShakeDetector();
        mSensorManager.registerListener(mShakeDetector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        try {
            sessionManager=new SessionManager(this);
             song_uri= Uri.fromFile(new File(sessionManager.getBackgroundMusic()));
            //added 09-04-21
            if(bg_mediaPlayer!=null) {
                bg_mediaPlayer.stop();
                //bg_mediaPlayer.release();
            }
            bg_mediaPlayer = new MediaPlayer();

            System.out.println("aji song playing");

            if(bg_mediaPlayer!=null) {
                final int sessionId = bg_mediaPlayer.getAudioSessionId();
                //audioSesionId = sessionManager.getSessionId();
                if(BackgroundMusicService.mequalizer!=null)
                 BackgroundMusicService.mequalizer.release();
                BackgroundMusicService.mequalizer = new Equalizer(0,sessionId);
                sessionManager.setSessionId(sessionId);
                System.out.println("aji Bg player audiosession "+sessionId);
            }
            bg_mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            bg_mediaPlayer.setDataSource(getApplicationContext(), song_uri);
            bg_mediaPlayer.prepare();
            //registerBecomingNoisyReceiver();




//            OnEqualizerChanged(sessionManager.getPosition());

//            mBassBoost = new BassBoost(0, bg_mediaPlayer.getAudioSessionId());
            try {
                mVirtualizer = new Virtualizer(0, bg_mediaPlayer.getAudioSessionId());
                mReverb = new PresetReverb(0,bg_mediaPlayer.getAudioSessionId());
//            showControllerInNotification();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

//            showNewNotification(this," String title", "String msg");
        } catch (IOException e) {
            e.printStackTrace();
        }



        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                System.out.println("aji shake >>>>>>>>>>>.  "+sessionManager.getShakeEnable());
                if(sessionManager.getShakeEnable()){

                    mSensorManager.unregisterListener(mShakeDetector);

                    System.out.println("aji shake "+Global.shake);
                    if(Global.shake) {
                        Global.shake = false;
                        if (BackgroundMusicService.bg_mediaPlayer != null) {

                            if (sessionManager.getSongPosition() + 1 == SongPlayingActivity.audioModelList.size()) {
                                    //sessionManager.setSongPosition(0);
                                    System.out.println("aji position equal to playing queue");
                                } else {

                                    int next_song_position = sessionManager.getSongPosition();
                                    next_song_position = next_song_position + 1;
                                    sessionManager.setSongPosition(next_song_position);
                                    System.out.println("aji nxt position " + next_song_position);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(SongPlayingActivity.audioModelList.get(sessionManager.getSongPosition()));
                                    sessionManager.setSong_Json(json);
                                int current_song_count = sessionManager.getSongPosition() + 1;
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(sessionManager.getSong_Json());
                                    sessionManager.setBackgroundMusic(jsonObject.optString("aPath"));


                                    System.out.println("aji shake playing path " + jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent stopserviceIntent = new Intent(BackgroundMusicService.this, BackgroundMusicService.class);
                                stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                                stopService(stopserviceIntent);
                                Intent serviceIntent = new Intent(BackgroundMusicService.this, BackgroundMusicService.class);
                                serviceIntent.putExtra("song_name", jsonObject.optString("track"));
                                serviceIntent.putExtra("artist_name", jsonObject.optString("aArtist"));
                                serviceIntent.putExtra("song_count", current_song_count + "/" + SongPlayingActivity.audioModelList.size());
                                serviceIntent.putExtra("album_art", jsonObject.optString("aAlbumart"));
                                serviceIntent.setAction(Constants.ACTION.NEXT_ACTION);
                                startService(serviceIntent);

                                try {
                                    jsonObject = new JSONObject(sessionManager.getSong_Json());
                                    HomeScreen.playing_song_artist.setText(jsonObject.optString("aArtist"));
                                    HomeScreen.playing_song_folder.setText(jsonObject.optString("aAlbum"));
                                    HomeScreen.playing_song_name.setText(jsonObject.optString("track"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (BackgroundMusicService.bg_mediaPlayer != null) {
                                    System.out.println("aji bg notnull");
                                    if (BackgroundMusicService.bg_mediaPlayer.isPlaying()) {
                                        System.out.println("aji playing");
                                        HomeScreen.play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                                    } else {
                                        System.out.println("aji bg notnull");
                                        HomeScreen.play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                                    }
                                } else {
                                    System.out.println("aji bg null");
                                    // Global.intent_method(PlaylistSongsDisplay.this, SongPlayingActivity.class);
                                }


                            }

//                    if (BackgroundMusicService.bg_mediaPlayer.isPlaying())
//                BackgroundMusicService.bg_mediaPlayer.pause();
//                    else
//                        BackgroundMusicService.bg_mediaPlayer.start();


                        }
//                    mSensorManager.registerListener(mShakeDetector,
//                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                        SensorManager.SENSOR_DELAY_UI);

                    }
                    //System.out.println("aji shake false");
                    BackgroundMusicService.mSensorManager.registerListener(BackgroundMusicService.mShakeDetector,
                            BackgroundMusicService.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            SensorManager.SENSOR_DELAY_UI);
                    Global.delay();
                }







//                mSensorManager.registerListener(mShakeDetector,
//                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                        SensorManager.SENSOR_DELAY_UI);

            }
        });


//        else
//        {
//            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//            int audioSessionId1 = audioManager.generateAudioSessionId();
//            System.out.println("aji BAckground audiomanager  "+audioSessionId1);
//
//            sessionManager.setSessionId(audioSessionId1);
//        }
//        mequalizer = new Equalizer(0, sessionManager.getSessionId());
//        System.out.println("aji  equalizer initalized");

    }
    public void onDestroy()
    {
        if (BackgroundMusicService.mequalizer != null) {
            BackgroundMusicService.mequalizer.release();
        }
        bg_mediaPlayer.stop();
//        unregisterReceiver(becomingNoisyReceiver);
        mSensorManager.unregisterListener(mShakeDetector);


         unregisterReceiver(myReceiver);
    }
    public void onStart(Intent intent,int startid){

//        Log.d(tag, "On start");
        bg_mediaPlayer.start();
    }

//    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //pause audio on ACTION_AUDIO_BECOMING_NOISY
//            //bg_mediaPlayer.stop();
//           bg_mediaPlayer.pause();
//        }
//    };

//    private void registerBecomingNoisyReceiver() {
//        //register after getting audio focus
//        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//        registerReceiver(becomingNoisyReceiver, intentFilter);
//    }





//    @Override
//    public void onAudioFocusChange(int focusState) {
//        //Invoked when the audio focus of the system is updated.
//        switch (focusState) {
////            case AudioManager.AUDIOFOCUS_GAIN:
////                // resume playback
////                if (mediaPlayer == null) initMediaPlayer();
////                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
////                mediaPlayer.setVolume(1.0f, 1.0f);
////                break;
//            case AudioManager.AUDIOFOCUS_LOSS:
//                // Lost focus for an unbounded amount of time: stop playback and release media player
//                if (bg_mediaPlayer.isPlaying()) bg_mediaPlayer.stop();
//                bg_mediaPlayer.release();
//                bg_mediaPlayer = null;
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                // Lost focus for a short time, but we have to stop
//                // playback. We don't release the media player because playback
//                // is likely to resume
//                if (bg_mediaPlayer.isPlaying()) bg_mediaPlayer.pause();
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                // Lost focus for a short time, but it's ok to keep playing
//                // at an attenuated level
//                if (bg_mediaPlayer.isPlaying()) bg_mediaPlayer.setVolume(0.1f, 0.1f);
//                break;
//        }
//    }










//    private boolean requestAudioFocus() {
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int result = audioManager.requestAudioFocus(, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            //Focus gained
//            return true;
//        }
//        //Could not gain focus
//        return false;
//    }
//
//    private boolean removeAudioFocus() {
//        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
//                audioManager.abandonAudioFocus(this);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
//        if (requestAudioFocus() == false) {
//            //Could not gain focus
//            stopSelf();
//        }

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION))
        {
//          showNotification();



            song_name=intent.getStringExtra("song_name");
            artist_name=intent.getStringExtra("artist_name");
            song_count=intent.getStringExtra("song_count");
            album_art=intent.getStringExtra("album_art");

            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
            bg_mediaPlayer.start();
            show_sample_notification();
            progressHandler();
            Log.e("aji","SERVICE on start command "+bg_mediaPlayer.getCurrentPosition()+" "+bg_mediaPlayer.getDuration());
        }
        else if(intent.getAction().equals(Constants.ACTION.PREV_ACTION))
        {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i("LOG_TAG", "Clicked Previous");
            try {
                bg_mediaPlayer.start();
                bg_mediaPlayer.setLooping(false);
                progressHandler();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION))
        {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i("LOG_TAG", "Clicked Play");
//            if(bg_mediaPlayer.isPlaying())
//            {
//                collapsedView.setImageViewResource(R.id.play_button_icon, R.drawable.ic_pause_black_24dp);
//                notificationBuilder.setCustomContentView(collapsedView);
//            }
//            else
//            {
//                collapsedView.setImageViewResource(R.id.play_button_icon, R.drawable.ic_play_arrow_black_24dp);
//                notificationBuilder.setCustomContentView(collapsedView);
//            }
            if(bg_mediaPlayer.isPlaying())
            {
                bg_mediaPlayer.pause();
//                collapsedView.setImageViewResource(R.id.play_button_icon, R.drawable.ic_play_arrow_black_24dp);
//                notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.app_logo)
//                        .setCustomContentView(collapsedView)
//                        .setCustomBigContentView(expandedView)
//                        .setContentIntent(pendingIntent)
////            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                        .build();
            }
            else
            {
                bg_mediaPlayer.start();
//                collapsedView.setImageViewResource(R.id.play_button_icon, R.drawable.ic_pause_black_24dp);
            }
        }
        else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION))
        {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i("aji", "Clicked Next");
            Log.e("aji","SERVICE on next "+bg_mediaPlayer.getCurrentPosition()+" "+bg_mediaPlayer.getDuration());
            System.out.println("aji Service next "+bg_mediaPlayer.getCurrentPosition()+" "+bg_mediaPlayer.getDuration());
            try {
                System.out.println("aji inside try");
               // bg_mediaPlayer.stop();
                bg_mediaPlayer.start();
                bg_mediaPlayer.setLooping(false);
                progressHandler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION))
        {
            Log.i("aji", "Received Stop Foreground Intent");
            System.out.println("aji service stopped");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_LONG).show();
            stopForeground(true);
            stopSelf();
        }
        else if (intent.getAction().equals(Constants.ACTION.PAUSE))
        {
            Log.i("aji", "Received Stop Foreground PAUSE");
            System.out.println("aji service stopped");
           // Toast.makeText(this, "Service Stoped", Toast.LENGTH_LONG).show();
            //stopForeground(true);
            //stopSelf();
           // bg_mediaPlayer.start();
        }


//        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
//            int state = intent.getIntExtra("state", -1);
//            switch (state) {
//                case 0:
//                    Log.d("aji", "Headset is unplugged");
//                    if(bg_mediaPlayer!= null)
//                    {
//                        if(bg_mediaPlayer.isPlaying() && sessionManager.getisplugged())
//                            bg_mediaPlayer.pause();
////
//                    }
//                    sessionManager.setisplugged(false);
//                    break;
//                case 1:
//                    Log.d("aji", "Headset is plugged");
//                    sessionManager.setisplugged(true);
//                   // bg_mediaPlayer.start();
//                    break;
//                default:
//                    Log.d("aji", "I have no idea what the headset state is");
//            }
//        }

        return START_STICKY;
    }


    public static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = "Channel_id";

            // The user-visible name of the channel.
            CharSequence channelName = "Music App";
            // The user-visible description of the channel.
            String channelDescription = "Music App Alert";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = true;
            //            int channelLockscreenVisibility = Notification.;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            //            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }

public void show_sample_notification()
{

    Intent notificationIntent = new Intent(this, SongPlayingActivity.class);
    notificationIntent.putExtra("value",2);
    notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
     pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    Intent previousIntent = new Intent(this, BackgroundMusicService.class);
    previousIntent.setAction(Constants.ACTION.PREV_ACTION);
    PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

    Intent playIntent = new Intent(this, BackgroundMusicService.class);
    playIntent.setAction(Constants.ACTION.PLAY_ACTION);
    PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

    Intent nextIntent = new Intent(this, BackgroundMusicService.class);
    nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
    PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

    Intent closeIntent = new Intent(this, BackgroundMusicService.class);
    closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
    PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);




    collapsedView = new RemoteViews(getPackageName(), R.layout.notification_layout_collapsed);
     expandedView = new RemoteViews(getPackageName(), R.layout.notification_layout);

    collapsedView.setOnClickPendingIntent(R.id.notification_play_icon, pplayIntent);

    Intent clickIntent = new Intent(this, NotificationReceiver.class);
    PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this, 0, clickIntent, 0);
    collapsedView.setTextViewText(R.id.app_name, getResources().getString(R.string.app_name));
    if(bg_mediaPlayer.isPlaying())
    {
        collapsedView.setImageViewResource(R.id.play_button_icon, R.drawable.ic_pause_black_24dp);
    }
    else
    {
        collapsedView.setImageViewResource(R.id.play_button_icon, R.drawable.ic_play_arrow_black_24dp);
    }

    expandedView.setImageViewResource(R.id.notification_image, R.drawable.app_logo);
    expandedView.setOnClickPendingIntent(R.id.drop_animation, clickPendingIntent);

    String channel_id=createNotificationChannel(this);
    CharSequence name = "Test";
    int importance = NotificationManager.IMPORTANCE_HIGH;

    NotificationChannel mChannel = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mChannel = new NotificationChannel(channel_id, name, importance);
    }


            notificationBuilder = new NotificationCompat.Builder(this,channel_id)
            .setSmallIcon(R.drawable.app_logo)
            .setContentIntent(pendingIntent)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setChannelId(channel_id)
            .setContentIntent(pendingIntent);
//    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel_id)
//            .setSmallIcon(R.drawable.app_logo)
//            .setCustomContentView(collapsedView)
//            .setCustomBigContentView(expandedView)
//            .setContentIntent(pendingIntent)
////            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//            .build();
    notificationManager.notify(1,notificationBuilder.build());
    if(album_art!=null && !album_art.equals(""))
    {

        Bitmap bm= BitmapFactory.decodeFile(album_art);
        int color=getDominantColor(bm);

        Palette palette=Palette.from(bm).generate();
        int vibrant = palette.getVibrantColor(0x000000);
        Log.e("abbas",""+getDominantColor(bm));
//            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        try
        {

            int c=0xff000000 + Integer.parseInt(Integer.toHexString(color),16);
            notificationBuilder.setColor(vibrant);
            Log.e("abbas"," "+vibrant);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}






    private void showNotification() {

        try
        {
            // Using RemoteViews to bind custom layouts into Notification
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_layout);
//        RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

// showing default album image
//        bigViews.setImageViewBitmap(R.id.status_bar_album_art,Constants.getDefaultAlbumArt(this));

            Intent notificationIntent = new Intent(this, SongPlayingActivity.class);
            notificationIntent.putExtra("value",2);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Intent previousIntent = new Intent(this, BackgroundMusicService.class);
            previousIntent.setAction(Constants.ACTION.PREV_ACTION);
            PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

            Intent playIntent = new Intent(this, BackgroundMusicService.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
            PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

            Intent nextIntent = new Intent(this, BackgroundMusicService.class);
            nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
            PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

            Intent closeIntent = new Intent(this, BackgroundMusicService.class);
            closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

            views.setOnClickPendingIntent(R.id.notification_play_icon, pplayIntent);
//        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

            views.setOnClickPendingIntent(R.id.notification_next_button, pnextIntent);
//        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

//            views.setOnClickPendingIntent(R.id.notification_previous_button, ppreviousIntent);
//        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

            views.setOnClickPendingIntent(R.id.drop_animation, pcloseIntent);
//        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

            views.setTextViewText(R.id.notification_play_icon,
                    getString(R.string.fa_pause));
//        bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);

            views.setTextViewText(R.id.notification_song_name, song_name);
//        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

            views.setTextViewText(R.id.notification_song_brand, artist_name);
            views.setTextViewText(R.id.song_count, song_count);
            if(album_art!=null && !album_art.equals(""))
            {
                views.setViewVisibility(R.id.album_art, View.VISIBLE);
//            views.setViewVisibility(R.id.no_audio_image, View.GONE);
                Bitmap bm= BitmapFactory.decodeFile(album_art);
                views.setImageViewBitmap(R.id.album_art,bm);
                int color=getDominantColor(bm);
                views.setInt(R.id.song_layout, "setBackgroundColor", color);
                Log.e("abbas",""+getDominantColor(bm));
//            Integer.toHexString(color);
//            0xff000000 + Integer.parseInt(hexVal,16)
//            views.col
            }
            else
            {
                views.setViewVisibility(R.id.album_art, View.GONE);
//            views.setViewVisibility(R.id.no_audio_image, View.VISIBLE);
            }

//        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

//        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

//       Notification status = new Notification.Builder(this).build();
//        status.contentView = views;
////        status.bigContentView = bigViews;
//        status.flags = Notification.FLAG_ONGOING_EVENT;
//        status.icon = R.drawable.app_logo;
//        status.contentIntent = pendingIntent;

            String channel_id=createNotificationChannel(this);
            CharSequence name = "Test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(channel_id, name, importance);
            }



            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel_id)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContent(views)
                    .setCustomBigContentView(views)
                    .setChannelId(channel_id)
                    .setContentIntent(pendingIntent);

//        notificationBuilder.setSound(defaultSoundUri)

            if(album_art!=null && !album_art.equals(""))
            {

                Bitmap bm= BitmapFactory.decodeFile(album_art);
                int color=getDominantColor(bm);

                Palette palette=Palette.from(bm).generate();
                int vibrant = palette.getVibrantColor(0x000000);
                Log.e("abbas",""+getDominantColor(bm));
//            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                try
                {

                    int c=0xff000000 + Integer.parseInt(Integer.toHexString(color),16);
                    notificationBuilder.setColor(vibrant);
                    Log.e("abbas"," "+vibrant);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }



            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(mChannel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



//        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);

        ValueAnimator va = ValueAnimator.ofInt(1, targetHeight);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        va.setDuration(300);
        va.setInterpolator(new OvershootInterpolator());
        va.start();
    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        ValueAnimator va = ValueAnimator.ofInt(initialHeight, 0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.GONE);
            }

            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        va.setDuration(300);
        va.setInterpolator(new DecelerateInterpolator());
        va.start();
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (bitmap == null) {
            return Color.TRANSPARENT;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];
        //Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int color;
        int r = 0;
        int g = 0;
        int b = 0;
        int a;
        int count = 0;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];
            a = Color.alpha(color);
            if (a > 0) {
                r += Color.red(color);
                g += Color.green(color);
                b += Color.blue(color);
                count++;
            }
        }
        r /= count;
        g /= count;
        b /= count;
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        color = 0xFF000000 | r | g | b;
        return color;
    }

    public void progressHandler()
    {
        Log.e("abbas","SERVICE  progressHandler inside"+bg_mediaPlayer.getCurrentPosition()+" "+bg_mediaPlayer.getDuration());
        hdlr.postDelayed(UpdateSongTime, 100);

        BackgroundMusicService.bg_mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                if(sessionManager.getIsRepeatSong())
                {
                    Log.e("abbas","new  MediaPlayerModel(0,\"\",3)");
                    EventBus.getDefault().post(new  MediaPlayerModel(0,"",3));
                }
                else if(sessionManager.getIsRepeatWholeSongs())
                {
                    EventBus.getDefault().post(new  MediaPlayerModel(0,"",4));
                }
                else
                {
                    Log.e("abbas","new  MediaPlayerModel(0,\"\",2)");
                    EventBus.getDefault().post(new  MediaPlayerModel(0,"",2));
                }
            }
        });

    }

    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
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

            Log.e("abbas","SERVICE  "+bg_mediaPlayer.getCurrentPosition()+" "+bg_mediaPlayer.getDuration());
            EventBus.getDefault().post(new  MediaPlayerModel(bg_mediaPlayer.getCurrentPosition(),minutes_string+" : "+seconds_string+" | "+song_minutes_string+" : "+song_seconds_string,1));
//            timing_textview.setText();
//
           // circularSeekBar.setProgress(BackgroundMusicService.bg_mediaPlayer.getCurrentPosition());
            if(BackgroundMusicService.bg_mediaPlayer.getCurrentPosition()==BackgroundMusicService.bg_mediaPlayer.getDuration())
            {
               // play_next_song();
                System.out.println("aji song end next song please");
            }
            hdlr.postDelayed(this, 100);
        }
    };

    public static void OnEqualizerChanged(int position)
    {
        try {
            if (position != 0) {
                mequalizer.usePreset((short) (position - 1));
                EqualizerSettings.presetPos = position;
                short numberOfFreqBands = 5;

                final short lowerEqualizerBandLevel = mequalizer.getBandLevelRange()[0];

                for (short i = 0; i < numberOfFreqBands; i++)
                {
                    EqualizerSeekbarModel equalizerSeekbarModel=new EqualizerSeekbarModel();
                    equalizerSeekbarModel.setPresetPos(i);
                    equalizerSeekbarModel.setLowerEqualizerBandLevel(lowerEqualizerBandLevel);
                    EventBus.getDefault().post(new EqualizerSeekbarModel());

                    //points[i]                                  = mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel;
                    EqualizerSettings.seekbarpos[i]                     = mequalizer.getBandLevel(i);
                    EqualizerSettings.equalizerModel.getSeekbarpos()[i] = mequalizer.getBandLevel(i);
                }
//                dataset.updateValues(points);
//                chart.notifyDataUpdate();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        EqualizerSettings.equalizerModel.setPresetPos(position);



    }

}