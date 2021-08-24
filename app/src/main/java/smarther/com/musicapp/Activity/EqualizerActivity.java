package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import smarther.com.musicapp.Model.EqualizerModel;
import smarther.com.musicapp.Model.EqualizerSeekbarModel;
import smarther.com.musicapp.Model.MediaPlayerModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Service.BackgroundMusicService;
import smarther.com.musicapp.Utils.Constants;
import smarther.com.musicapp.Utils.EqualizerSettings;
import smarther.com.musicapp.Utils.EqualizerSettingsObject;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;
import smarther.com.musicapp.Utils.VerticalSeekBar;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

public class EqualizerActivity extends AppCompatActivity {
//    Equalizer mEqualizer;
//    https://github.com/psaravan/JamsMusicPlayer/blob/master/jamsMusicPlayer/src/main/java/com/jams/music/player/EqualizerActivity/EqualizerActivity.java

//    Temp variables that hold the equalizer's settings.

    short   numberOfFrequencyBands;
    int y=0;


    private int fiftyHertzLevel = 16;
    private int oneThirtyHertzLevel = 16;
    private int threeTwentyHertzLevel = 16;
    private int eightHundredHertzLevel = 16;
    private int twoKilohertzLevel = 16;
    private int fiveKilohertzLevel = 16;
    private int twelvePointFiveKilohertzLevel = 16;

   private static SeekBar[] seekBarFinal = new SeekBar[5];
    VerticalSeekBar first_seek_bar;
    VerticalSeekBar second_seek_bar;
    VerticalSeekBar third_seek_bar;
    VerticalSeekBar fourth_seek_bar;
    VerticalSeekBar fifth_seek_bar;
    Croller virtualizer_croller;
    Croller bassboost_croller;
    Switch equalizer_switch;
    LinearLayout back;
    // Temp variables that hold audio fx settings.
    private int virtualizerLevel;
    private int bassBoostLevel;
    private int reverbSetting;

    LinearLayout none_equalizer;
    LinearLayout small_room_equalizer;
    LinearLayout medium_room_equalizer;
    LinearLayout large_room_equalizer;
    LinearLayout medium_hall_equalizer;
    LinearLayout large_hall_equalizer;
    LinearLayout plate_equalizer;
    LinearLayout equalizer_layout;

   public LinearLayout normal_layout;
    public LinearLayout classical_layout;
    public  LinearLayout dance_layout;
    public  LinearLayout flat_layout;
    public  LinearLayout folk_layout;
    public  LinearLayout heavy_metal_layout;
    public  LinearLayout hip_hop_layout;
    public   LinearLayout jazz_layout;
    public     LinearLayout pop_layout;
    public  LinearLayout rock_layout;
    public LinearLayout custom_layout;
    SessionManager sessionManager;
    private MediaPlayer mediaPlayer;
    View unselected_view;
//    public static final String ARG_AUDIO_SESSIOIN_ID = "audio_session_id";
    private int     audioSesionId;
    public BassBoost bassBoost;
    public PresetReverb presetReverb;
    public Virtualizer virtualizer;
    int position;
    Context context;
    //public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);
        sessionManager = new SessionManager(EqualizerActivity.this);
        loadEqualizerSettings();
       // myReceiver = new MusicIntentReceiver();

//        if(sessionManager.getPosition()!=-1) {
//            entryEqualizerselected(sessionManager.getPosition());
//        }

        context = getApplicationContext();
        System.out.println("aji eqlizer start");
        back = findViewById(R.id.back);

//        first_seek_bar=(VerticalSeekBar)findViewById(R.id.first_seek_bar);
//        seekBarFinal[0]=first_seek_bar;
//        second_seek_bar=(VerticalSeekBar)findViewById(R.id.second_seek_bar);
//        seekBarFinal[1]=second_seek_bar;
//        third_seek_bar=(VerticalSeekBar)findViewById(R.id.third_seek_bar);
//        seekBarFinal[2]=third_seek_bar;
//        fourth_seek_bar=(VerticalSeekBar)findViewById(R.id.fourth_seek_bar);
//        seekBarFinal[3]=fourth_seek_bar;
//        fifth_seek_bar=(VerticalSeekBar)findViewById(R.id.fifth_seek_bar);
//        seekBarFinal[4]=fifth_seek_bar;
        equalizer_switch=(Switch)findViewById(R.id.equalizer_switch);
        virtualizer_croller=(Croller)findViewById(R.id.virtualizer_croller);
        bassboost_croller=(Croller)findViewById(R.id.bassboost_croller);




        none_equalizer=(LinearLayout)findViewById(R.id.none_equalizer);
        small_room_equalizer=(LinearLayout)findViewById(R.id.small_room_equalizer);
        medium_room_equalizer=(LinearLayout)findViewById(R.id.medium_room_equalizer);
        large_room_equalizer=(LinearLayout)findViewById(R.id.large_room_equalizer);
        medium_hall_equalizer=(LinearLayout)findViewById(R.id.medium_hall_equalizer);
        large_hall_equalizer=(LinearLayout)findViewById(R.id.large_hall_equalizer);
        plate_equalizer=(LinearLayout)findViewById(R.id.plate_equalizer);
        equalizer_layout=(LinearLayout)findViewById(R.id.equalizer_layout);

        normal_layout=(LinearLayout)findViewById(R.id.normal_layout);
        classical_layout=(LinearLayout)findViewById(R.id.classical_layout);
        dance_layout=(LinearLayout)findViewById(R.id.dance_layout);
        flat_layout=(LinearLayout)findViewById(R.id.flat_layout);
        folk_layout=(LinearLayout)findViewById(R.id.folk_layout);
        heavy_metal_layout=(LinearLayout)findViewById(R.id.heavy_metal_layout);
        hip_hop_layout=(LinearLayout)findViewById(R.id.hip_hop_layout);
        jazz_layout=(LinearLayout)findViewById(R.id.jazz_layout);
        pop_layout=(LinearLayout)findViewById(R.id.pop_layout);
        rock_layout=(LinearLayout)findViewById(R.id.rock_layout);
        custom_layout=(LinearLayout)findViewById(R.id.custom_layout);
       // mediaPlayer = MediaPlayer.create(this);

//        if(sessionManager.getPosition()!=-1) {
//            System.out.println("aji position" + sessionManager.getPosition());
//            entryEqualizerselected(sessionManager.getPosition());
//            final short lowerEqualizerBandLevel = -1500;
//            final short upperEqualizerBandLevel = 1500;
//            for (short i = 0; i < numberOfFrequencyBands; i++) {
//                final short equalizerBandIndex = i;
//                SeekBar seekBar = new SeekBar(context);
//                switch (i) {
//                    case 0:
//                        seekBar = findViewById(R.id.seekBar1);
//                        break;
//                    case 1:
//                        seekBar = findViewById(R.id.seekBar2);
//                        break;
//                    case 2:
//                        seekBar = findViewById(R.id.seekBar3);
//                        break;
//                    case 3:
//                        seekBar = findViewById(R.id.seekBar4);
//                        break;
//                    case 4:
//                        seekBar = findViewById(R.id.seekBar5);
//                        break;
//                }
//                seekBarFinal[i] = seekBar;
//                seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN));
//                seekBar.setId(i);
////            seekBar.setLayoutParams(layoutParams);
//                seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
//
////
////                if (EqualizerSettings.isEqualizerReloaded) {
////
////                    seekBar.setProgress(EqualizerSettings.seekbarpos[i]);
////                } else {
////                    seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex));
////                    EqualizerSettings.seekbarpos[i] = mEqualizer.getBandLevel(equalizerBandIndex);
////                    EqualizerSettings.isEqualizerReloaded = true;
////                }
//            }
//        }

//        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        int audioSessionId1 = audioManager.generateAudioSessionId();
//        System.out.println("aji audiomanager "+audioSessionId1 );
        if(sessionManager.getSessionId()!=-1)
        {
            if(BackgroundMusicService.mequalizer!=null) //added
            BackgroundMusicService.mequalizer.release();
            audioSesionId = sessionManager.getSessionId();
           BackgroundMusicService.mequalizer = new Equalizer(0,audioSesionId);
//            BackgroundMusicService.mReverb = new PresetReverb(0,audioSesionId);

        }
        else
        {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
         audioSesionId = audioManager.generateAudioSessionId();
            sessionManager.setSessionId(audioSesionId);
            BackgroundMusicService.mequalizer = new Equalizer(0,audioSesionId);
//            BackgroundMusicService.mReverb = new PresetReverb(0,audioSesionId);
//            call_preverb(1);


        }
        if(sessionManager.getPreverbPosition()!=-1)
        {
            call_preverb(sessionManager.getPreverbPosition());
        }
        else
        {
            call_preverb(1);

        }
        if(sessionManager.getPosition()!=-1) {
            System.out.println("aji eq position" + sessionManager.getPosition());
            entryEqualizerselected(sessionManager.getPosition());
        }
        else
        {
            System.out.println("aji default eq position" + sessionManager.getPosition());
            entryEqualizerselected(4);
        }

//        audioSesionId =  BackgroundMusicService.bg_mediaPlayer.getAudioSessionId();

        System.out.println("aji" + audioSesionId);



//        unselected_view=(View) findViewById(R.id.unselected_view);




//        first_seek_bar.setOnSeekBarChangeListener(equalizer50HzListener);
//        second_seek_bar.setOnSeekBarChangeListener(equalizer130HzListener);
//        third_seek_bar.setOnSeekBarChangeListener(equalizer320HzListener);
//        fourth_seek_bar.setOnSeekBarChangeListener(equalizer800HzListener);
//        fifth_seek_bar.setOnSeekBarChangeListener(equalizer2kHzListener);


        //virtualizer_croller.setOnCrollerChangeListener(virtualizerListener);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        equalizer_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(audioSesionId!=-1) {
                    BackgroundMusicService.mequalizer.setEnabled(isChecked);
                    bassboost_croller.setEnabled(isChecked);
                    virtualizer_croller.setEnabled(isChecked);
                    EqualizerSettings.isEqualizerEnabled = isChecked;
                    EqualizerSettings.equalizerModel.setEqualizerEnabled(isChecked);
                }
            }
        });

        virtualizer_croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                EqualizerSettings.reverbPreset = (short) ((progress * 6) / 19);
                EqualizerSettings.equalizerModel.setReverbPreset(EqualizerSettings.reverbPreset);
                try {
                  //  presetReverb.setPreset(EqualizerSettings.reverbPreset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                y = progress;

            }
        });
//        bassboost_croller.setOnCrollerChangeListener(bassBoostListener);
        bassboost_croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                EqualizerSettings.bassStrength = (short) (((float) 1000 / 19) * (progress));
                try {
                    bassBoost.setStrength(EqualizerSettings.bassStrength);
                    EqualizerSettings.equalizerModel.setBassStrength(EqualizerSettings.bassStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        if(audioSesionId!=-1) {
            equalizer_switch.setChecked(EqualizerSettings.isEqualizerEnabled);


            if (!EqualizerSettings.isEqualizerReloaded) {
                int x = 0;
                if (bassBoost != null) {
                    try {
                        x = ((bassBoost.getRoundedStrength() * 19) / 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                if (presetReverb != null) {
//                    try {
//                        y = (presetReverb.getPreset() * 19) / 6;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
            //    }

                if (x == 0) {
                    bassboost_croller.setProgress(1);
                } else {
                    bassboost_croller.setProgress(x);
                }

                if (y == 0) {
                    virtualizer_croller.setProgress(1);
                } else {
                    virtualizer_croller.setProgress(y);
                }
            }
            else {
                int x = ((EqualizerSettings.bassStrength * 19) / 1000);
                y = (EqualizerSettings.reverbPreset * 19) / 6;
                if (x == 0) {
                    bassboost_croller.setProgress(1);
                } else {
                    bassboost_croller.setProgress(x);
                }

                if (y == 0) {
                    virtualizer_croller.setProgress(1);
                } else {
                    virtualizer_croller.setProgress(y);
                }
            }

            EqualizerSettings.isEditing = true;

//        if (getArguments() != null && getArguments().containsKey(ARG_AUDIO_SESSIOIN_ID)) {
//            audioSesionId = getArguments().getInt(ARG_AUDIO_SESSIOIN_ID);
//        }

//        if(audioSesionId!=-1) {
            if (EqualizerSettings.equalizerModel == null) {
                EqualizerSettings.equalizerModel = new EqualizerModel();
                EqualizerSettings.equalizerModel.setReverbPreset(PresetReverb.PRESET_NONE);
                EqualizerSettings.equalizerModel.setBassStrength((short) (1000 / 19));
            }

//            mEqualizer = new Equalizer(0, audioSesionId);
            bassBoost = new BassBoost(0, audioSesionId);
           // presetReverb = new PresetReverb(0, audioSesionId);
            bassBoost.setEnabled(EqualizerSettings.isEqualizerEnabled);
            BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
            BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
            bassBoostSetting.strength = EqualizerSettings.equalizerModel.getBassStrength();
            bassBoost.setProperties(bassBoostSetting);
//            presetReverb.setPreset(EqualizerSettings.equalizerModel.getReverbPreset());
        //    presetReverb.setEnabled(EqualizerSettings.isEqualizerEnabled);
            System.out.println("aji EqualizerSettings.isEqualizerEnabled "+EqualizerSettings.isEqualizerEnabled);
            //BackgroundMusicService.mequalizer.setEnabled(EqualizerSettings.isEqualizerEnabled);

//        }


            //---------------------------------------------------------------------------
            if ((EqualizerSettings.isEqualizerReloaded && EqualizerSettings.presetPos != 0) && audioSesionId!=-1) {
//            correctPosition = false;
//                BackgroundMusicService.OnEqualizerChanged(EqualizerSettings.presetPos);
                OnEqualizerChanged(EqualizerSettings.presetPos);

            }
            //-------------------------------------------------------------------------------


            if (EqualizerSettings.presetPos == 0) {
                for (short bandIdx = 0; bandIdx < BackgroundMusicService.mequalizer.getNumberOfBands(); bandIdx++) {
                    BackgroundMusicService.mequalizer.setBandLevel(bandIdx, (short) EqualizerSettings.seekbarpos[bandIdx]);
                }
            } else {
//                BackgroundMusicService.mequalizer.usePreset((short) EqualizerSettings.presetPos);
            }
            numberOfFrequencyBands = 5;

            final short lowerEqualizerBandLevel = BackgroundMusicService.mequalizer.getBandLevelRange()[0];
            final short upperEqualizerBandLevel = BackgroundMusicService.mequalizer.getBandLevelRange()[1];
            System.out.println("aji  lowerEqualizerBandLevel"+lowerEqualizerBandLevel);
            System.out.println("aji  upperEqualizerBandLevel"+upperEqualizerBandLevel);
            for (short i = 0; i < numberOfFrequencyBands; i++) {
                final short equalizerBandIndex = i;
                SeekBar seekBar = new SeekBar(context);
                switch (i) {
                    case 0:
                        seekBar = findViewById(R.id.seekBar1);
                        break;
                    case 1:
                        seekBar = findViewById(R.id.seekBar2);
                        break;
                    case 2:
                        seekBar = findViewById(R.id.seekBar3);
                        break;
                    case 3:
                        seekBar = findViewById(R.id.seekBar4);
                        break;
                    case 4:
                        seekBar = findViewById(R.id.seekBar5);
                        break;
                }
                seekBarFinal[i] = seekBar;
                seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN));
                seekBar.setId(i);
//            seekBar.setLayoutParams(layoutParams);
                seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);


                if (EqualizerSettings.isEqualizerReloaded) {

                    seekBar.setProgress(EqualizerSettings.seekbarpos[i] - lowerEqualizerBandLevel);
                } else {
                    seekBar.setProgress(BackgroundMusicService.mequalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
                    EqualizerSettings.seekbarpos[i] = BackgroundMusicService.mequalizer.getBandLevel(equalizerBandIndex);
                    EqualizerSettings.isEqualizerReloaded = true;
                }

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        System.out.println("ajii id " + seekBar.getId());
                        BackgroundMusicService.mequalizer.setBandLevel(equalizerBandIndex, (short) (progress + lowerEqualizerBandLevel));
                        EqualizerSettings.seekbarpos[seekBar.getId()] = (progress + lowerEqualizerBandLevel);
                        EqualizerSettings.equalizerModel.getSeekbarpos()[seekBar.getId()] = (progress + lowerEqualizerBandLevel);


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        System.out.println("ajii onSeek start----");
                        normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                        custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                        EqualizerSettings.presetPos = 0;
                        EqualizerSettings.equalizerModel.setPresetPos(0);
                        sessionManager.setPosition(0);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        System.out.println("ajii custom----");

                    }
                });

            }
        }
        else
        {
            System.out.println("ajii backgroundmusic player is null");
            if(sessionManager.getPosition()!=-1)
            {
                entryEqualizerselected(sessionManager.getPosition());
            }
            else
            {
                System.out.println("aji no session position");
            }
        }






















//Top Scroller Onclick Listener
        normal_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(1);
                    OnEqualizerChanged(1);
                    sessionManager.setPosition(1);

                }
                else{
                    sessionManager.setPosition(1);
                }



            }
        });
        classical_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(2);
                    OnEqualizerChanged(2);
                    sessionManager.setPosition(2);

                }
                else{
                    sessionManager.setPosition(2);
                }


            }
        });
        dance_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(3);
                    OnEqualizerChanged(3);
                    sessionManager.setPosition(3);

                }
                else{
                    sessionManager.setPosition(3);
                }

            }
        });
        flat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(4);
                    OnEqualizerChanged(4);
                    sessionManager.setPosition(4);

                }
                else{
                    sessionManager.setPosition(4);
                }

            }
        });
        folk_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(5);
                    OnEqualizerChanged(5);
                    sessionManager.setPosition(5);


                }
                else{
                    sessionManager.setPosition(5);
                }

            }
        });
        heavy_metal_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(6);
                    OnEqualizerChanged(6);
                    sessionManager.setPosition(6);

                }
                else{
                    sessionManager.setPosition(6);
                }
            }
        });
        hip_hop_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(7);
                    OnEqualizerChanged(7);
                    sessionManager.setPosition(7);

                }
                else{
                    sessionManager.setPosition(7);
                }
            }
        });
        jazz_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(8);
                    OnEqualizerChanged(8);
                    sessionManager.setPosition(8);

                }
                else{
                    sessionManager.setPosition(8);
                }
            }
        });
        pop_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
                    sessionManager.setPosition(9);
//                    BackgroundMusicService.OnEqualizerChanged(9);
                    OnEqualizerChanged(9);


                }
                else{
                    sessionManager.setPosition(9);
                }

            }
        });
        rock_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                if(audioSesionId!=-1)
                {
                    //BackgroundMusicService.OnEqualizerChanged(10);
                    OnEqualizerChanged(10);
                    sessionManager.setPosition(10);

                }
                else{
                    sessionManager.setPosition(10);
                }

            }
        });
        custom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
                custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
                if(audioSesionId!=-1)
                {
//                    BackgroundMusicService.OnEqualizerChanged(0);
                    OnEqualizerChanged(0);
                    sessionManager.setPosition(0);
                }
                else{
                    sessionManager.setPosition(0);
                }

            }
        });






//        equalizer_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//
//                mEqualizer.setEnabled(checked);
//                bassboost_croller.setEnabled(checked);
//                virtualizer_croller.setEnabled(checked);
//                EqualizerSettings.isEqualizerEnabled = checked;
//                EqualizerSettings.equalizerModel.setEqualizerEnabled(checked);
//                if(checked)
//                {
//
////                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    unselected_view.setVisibility(View.GONE);
//                    equalizer_layout.setEnabled(true);
//                    for (int i = 0; i < equalizer_layout.getChildCount(); i++) {
//                        View child = equalizer_layout.getChildAt(i);
//                        child.setEnabled(true);
//                    }
//                }
//                else
//                {
////                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    unselected_view.setVisibility(View.VISIBLE);
//                    equalizer_layout.setEnabled(false);
//                    for (int i = 0; i < equalizer_layout.getChildCount(); i++) {
//                        View child = equalizer_layout.getChildAt(i);
//                        child.setEnabled(false);
//                    }
//                }
//            }
//        });



        none_equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                call_preverb(1);

            }
        });

        small_room_equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                call_preverb(2);

            }
        });

        medium_room_equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_preverb(3);
            }
        });

        large_room_equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                call_preverb(4);
            }
        });

        medium_hall_equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                call_preverb(5);
            }
        });

        large_hall_equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                call_preverb(6);
            }
        });

        plate_equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                call_preverb(7);
            }
        });



    }


    private void saveEqualizerSettings(){
        if (EqualizerSettings.equalizerModel != null){
            EqualizerSettingsObject settings = new EqualizerSettingsObject();
            settings.bassStrength = EqualizerSettings.equalizerModel.getBassStrength();
            settings.presetPos = EqualizerSettings.equalizerModel.getPresetPos();
            settings.reverbPreset = EqualizerSettings.equalizerModel.getReverbPreset();
            settings.seekbarpos = EqualizerSettings.equalizerModel.getSeekbarpos();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            Gson gson = new Gson();
            preferences.edit()
                    .putString("equalizer", gson.toJson(settings))
                    .apply();
        }
    }

    private void loadEqualizerSettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        EqualizerSettingsObject settings = gson.fromJson(preferences.getString("equalizer", "{}"), EqualizerSettingsObject.class);
        EqualizerModel model = new EqualizerModel();
        model.setBassStrength(settings.bassStrength);
        model.setPresetPos(settings.presetPos);
        model.setReverbPreset(settings.reverbPreset);
        model.setSeekbarpos(settings.seekbarpos);

        EqualizerSettings.isEqualizerEnabled = true;
        EqualizerSettings.isEqualizerReloaded = true;
        EqualizerSettings.bassStrength = settings.bassStrength;
        EqualizerSettings.presetPos = settings.presetPos;
        EqualizerSettings.reverbPreset = settings.reverbPreset;
        EqualizerSettings.seekbarpos = settings.seekbarpos;
        EqualizerSettings.equalizerModel = model;
    }

 void OnEqualizerChanged(int position)
    {
        try {
            if (position != 0) {
                BackgroundMusicService.mequalizer.usePreset((short) (position - 1));
                EqualizerSettings.presetPos = position;
                short numberOfFreqBands = 5;

                final short lowerEqualizerBandLevel = BackgroundMusicService.mequalizer.getBandLevelRange()[0];

                for (short i = 0; i < numberOfFreqBands; i++) {
                    seekBarFinal[i].setProgress(BackgroundMusicService.mequalizer.getBandLevel(i) - lowerEqualizerBandLevel);
                    //points[i]                                  = mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel;
                    EqualizerSettings.seekbarpos[i]                     = BackgroundMusicService.mequalizer.getBandLevel(i);
                    EqualizerSettings.equalizerModel.getSeekbarpos()[i] = BackgroundMusicService.mequalizer.getBandLevel(i);
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



//    /**
//     * 50 Hz equalizer seekbar listener.
//     */
//    private SeekBar.OnSeekBarChangeListener equalizer50HzListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar arg0, int seekBarLevel, boolean changedByUser) {
//
//            try {
//                //Get the appropriate equalizer band.
//                short sixtyHertzBand = mEqualizer.getBand(50000);
//
//                //Set the gain level text based on the slider position.
//                if (seekBarLevel==16) {
////                    text50HzGainTextView.setText("0 dB");
//                    mEqualizer.setBandLevel(sixtyHertzBand, (short) 0);
//                } else if (seekBarLevel < 16) {
//
//                    if (seekBarLevel==0) {
////                        text50HzGainTextView.setText("-" + "15 dB");
//                        mEqualizer.setBandLevel(sixtyHertzBand, (short) (-1500));
//                    } else {
////                        text50HzGainTextView.setText("-" + (16-seekBarLevel) + " dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(sixtyHertzBand, (short) -((16-seekBarLevel)*100));
//                    }
//
//                } else if (seekBarLevel > 16) {
////                    text50HzGainTextView.setText("+" + (seekBarLevel-16) + " dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(sixtyHertzBand, (short) ((seekBarLevel-16)*100));
//                }
//
//                fiftyHertzLevel = seekBarLevel;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * 130 Hz equalizer seekbar listener.
//     */
//    private SeekBar.OnSeekBarChangeListener equalizer130HzListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar arg0, int seekBarLevel, boolean changedByUser) {
//
//            try {
//                //Get the appropriate equalizer band.
//                short twoThirtyHertzBand = BackgroundMusicService.mequalizer.getBand(130000);
//
//                //Set the gain level text based on the slider position.
//                if (seekBarLevel==16) {
////                    text130HzGainTextView.setText("0 dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(twoThirtyHertzBand, (short) 0);
//                } else if (seekBarLevel < 16) {
//
//                    if (seekBarLevel==0) {
////                        text130HzGainTextView.setText("-" + "15 dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(twoThirtyHertzBand, (short) (-1500));
//                    } else {
////                        text130HzGainTextView.setText("-" + (16-seekBarLevel) + " dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(twoThirtyHertzBand, (short) -((16-seekBarLevel)*100));
//                    }
//
//                } else if (seekBarLevel > 16) {
////                    text130HzGainTextView.setText("+" + (seekBarLevel-16) + " dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(twoThirtyHertzBand, (short) ((seekBarLevel-16)*100));
//                }
//
//                oneThirtyHertzLevel = seekBarLevel;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * 320 Hz equalizer seekbar listener.
//     */
//    private SeekBar.OnSeekBarChangeListener equalizer320HzListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar arg0, int seekBarLevel, boolean changedByUser) {
//
//            try {
//                //Get the appropriate equalizer band.
//                short nineTenHertzBand = BackgroundMusicService.mequalizer.getBand(320000);
//
//                //Set the gain level text based on the slider position.
//                if (seekBarLevel==16) {
////                    text320HzGainTextView.setText("0 dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(nineTenHertzBand, (short) 0);
//                } else if (seekBarLevel < 16) {
//
//                    if (seekBarLevel==0) {
////                        text320HzGainTextView.setText("-" + "15 dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(nineTenHertzBand, (short) (-1500));
//                    } else {
////                        text320HzGainTextView.setText("-" + (16-seekBarLevel) + " dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(nineTenHertzBand, (short) -((16-seekBarLevel)*100));
//                    }
//
//                } else if (seekBarLevel > 16) {
////                    text320HzGainTextView.setText("+" + (seekBarLevel-16) + " dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(nineTenHertzBand, (short) ((seekBarLevel-16)*100));
//                }
//
//                threeTwentyHertzLevel = seekBarLevel;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * 800 Hz equalizer seekbar listener.
//     */
//    private SeekBar.OnSeekBarChangeListener equalizer800HzListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar arg0, int seekBarLevel, boolean changedByUser) {
//
//            try {
//                //Get the appropriate equalizer band.
//                short threeKiloHertzBand = BackgroundMusicService.mequalizer.getBand(800000);
//
//                //Set the gain level text based on the slider position.
//                if (seekBarLevel==16) {
////                    text800HzGainTextView.setText("0 dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(threeKiloHertzBand, (short) 0);
//                } else if (seekBarLevel < 16) {
//
//                    if (seekBarLevel==0) {
////                        text800HzGainTextView.setText("-" + "15 dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(threeKiloHertzBand, (short) (-1500));
//                    } else {
////                        text800HzGainTextView.setText("-" + (16-seekBarLevel) + " dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(threeKiloHertzBand, (short) -((16-seekBarLevel)*100));
//                    }
//
//                } else if (seekBarLevel > 16) {
////                    text800HzGainTextView.setText("+" + (seekBarLevel-16) + " dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(threeKiloHertzBand, (short) ((seekBarLevel-16)*100));
//                }
//
//                eightHundredHertzLevel = seekBarLevel;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * 2 kHz equalizer seekbar listener.
//     */
//    private SeekBar.OnSeekBarChangeListener equalizer2kHzListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar arg0, int seekBarLevel, boolean changedByUser) {
//
//            try {
//                //Get the appropriate equalizer band.
//                short fourteenKiloHertzBand = BackgroundMusicService.mequalizer.getBand(2000000);
//
//                //Set the gain level text based on the slider position.
//                if (seekBarLevel==16) {
////                    text2kHzGainTextView.setText("0 dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(fourteenKiloHertzBand, (short) 0);
//                } else if (seekBarLevel < 16) {
//
//                    if (seekBarLevel==0) {
////                        text2kHzGainTextView.setText("-" + "15 dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(fourteenKiloHertzBand, (short) (-1500));
//                    } else {
////                        text2kHzGainTextView.setText("-" + (16-seekBarLevel) + " dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(fourteenKiloHertzBand, (short) -((16-seekBarLevel)*100));
//                    }
//
//                } else if (seekBarLevel > 16) {
////                    text2kHzGainTextView.setText("+" + (seekBarLevel-16) + " dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(fourteenKiloHertzBand, (short) ((seekBarLevel-16)*100));
//                }
//
//                twoKilohertzLevel = seekBarLevel;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * 5 kHz equalizer seekbar listener.
//     */
//    private SeekBar.OnSeekBarChangeListener equalizer5kHzListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar arg0, int seekBarLevel, boolean changedByUser) {
//
//            try {
//                //Get the appropriate equalizer band.
//                short fiveKiloHertzBand = BackgroundMusicService.mequalizer.getBand(5000000);
//
//                //Set the gain level text based on the slider position.
//                if (seekBarLevel==16) {
////                    text5kHzGainTextView.setText("0 dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(fiveKiloHertzBand, (short) 0);
//                } else if (seekBarLevel < 16) {
//
//                    if (seekBarLevel==0) {
////                        text5kHzGainTextView.setText("-" + "15 dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(fiveKiloHertzBand, (short) (-1500));
//                    } else {
////                        text5kHzGainTextView.setText("-" + (16-seekBarLevel) + " dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(fiveKiloHertzBand, (short) -((16-seekBarLevel)*100));
//                    }
//
//                } else if (seekBarLevel > 16) {
////                    text5kHzGainTextView.setText("+" + (seekBarLevel-16) + " dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(fiveKiloHertzBand, (short) ((seekBarLevel-16)*100));
//                }
//
//                fiveKilohertzLevel = seekBarLevel;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * 12.5 kHz equalizer seekbar listener.
//     */
//    private SeekBar.OnSeekBarChangeListener equalizer12_5kHzListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar arg0, int seekBarLevel, boolean changedByUser) {
//
//            try {
//                //Get the appropriate equalizer band.
//                short twelvePointFiveKiloHertzBand = BackgroundMusicService.mequalizer.getBand(9000000);
//
//                //Set the gain level text based on the slider position.
//                if (seekBarLevel==16) {
////                    text12_5kHzGainTextView.setText("0 dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(twelvePointFiveKiloHertzBand, (short) 0);
//                } else if (seekBarLevel < 16) {
//
//                    if (seekBarLevel==0) {
////                        text12_5kHzGainTextView.setText("-" + "15 dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(twelvePointFiveKiloHertzBand, (short) (-1500));
//                    } else {
////                        text12_5kHzGainTextView.setText("-" + (16-seekBarLevel) + " dB");
//                        BackgroundMusicService.mequalizer.setBandLevel(twelvePointFiveKiloHertzBand, (short) -((16-seekBarLevel)*100));
//                    }
//
//                } else if (seekBarLevel > 16) {
////                    text12_5kHzGainTextView.setText("+" + (seekBarLevel-16) + " dB");
//                    BackgroundMusicService.mequalizer.setBandLevel(twelvePointFiveKiloHertzBand, (short) ((seekBarLevel-16)*100));
//                }
//
//                twelvePointFiveKilohertzLevel = seekBarLevel;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * Spinner listener for reverb effects.
//     */
//    private AdapterView.OnItemSelectedListener reverbListener = new AdapterView.OnItemSelectedListener() {
//
//        @Override
//        public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
//
//            if (index==0)
//            {
//                    BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_NONE);
//                    reverbSetting = 0;
//                }
//            else if (index==1)
//            {
//                    BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
//                    reverbSetting = 1;
//                }
//            else if (index==2)
//            {
//                    BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_LARGEROOM);
//                    reverbSetting = 2;
//                }
//            else if (index==3)
//            {
//                    BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_MEDIUMHALL);
//                    reverbSetting = 3;
//                }
//            else if (index==4)
//            {
//                    BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_MEDIUMROOM);
//                    reverbSetting = 4;
//                }
//            else if (index==5)
//            {
//                    BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_SMALLROOM);
//                    reverbSetting = 5;
//                }
//            else if (index==6)
//            {
//                    BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_PLATE);
//                    reverbSetting = 6;
//                }
//
//                else
//                    reverbSetting = 0;
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//    };
//
//    /**
//     * Bass boost listener.
//     */
//    private OnCrollerChangeListener bassBoostListener = new OnCrollerChangeListener() {
//
//        @Override
//        public void onProgressChanged(Croller croller, int progress) {
//        //    BackgroundMusicService.mBassBoost.setStrength((short) progress);
//          //  bassBoostLevel = (short) progress;
//            EqualizerSettings.bassStrength = (short) (((float) 1000 / 19) * (progress));
//            try {
//                bassBoost.setStrength(EqualizerSettings.bassStrength);
//                EqualizerSettings.equalizerModel.setBassStrength(EqualizerSettings.bassStrength);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onStartTrackingTouch(Croller croller) {
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(Croller croller) {
//
//        }
//    };
//
//    /**
//     * Virtualizer listener.
//     */
//    private OnCrollerChangeListener virtualizerListener = new OnCrollerChangeListener() {
//
//        @Override
//        public void onProgressChanged(Croller croller, int progress) {
//            //BackgroundMusicService.mVirtualizer.setStrength((short) progress);
//           // virtualizerLevel = (short) progress;
//            EqualizerSettings.reverbPreset = (short) ((progress * 6) / 19);
//            EqualizerSettings.equalizerModel.setReverbPreset(EqualizerSettings.reverbPreset);
//            try {
//                presetReverb.setPreset(EqualizerSettings.reverbPreset);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            y = progress;
//        }
//
//        @Override
//        public void onStartTrackingTouch(Croller croller) {
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(Croller croller) {
//
//        }
//    };

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (BackgroundMusicService.mequalizer != null) {
//            BackgroundMusicService.mequalizer.release();
//        }

//        if (bassBoost != null) {
//            bassBoost.release();
//        }

//        if (presetReverb != null) {
//            presetReverb.release();
//        }

        EqualizerSettings.isEditing = false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void call_preverb(int value)
    {
        sessionManager.setPreverbPosition(value);

        System.out.println("aji rooms"+value);
        if(value==1)
        {
            none_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
            small_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            plate_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            if(BackgroundMusicService.mReverb!=null)
            {
                System.out.println("aji mreverb not null");
                BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_NONE);
            }
        }
        else if(value==2)
        {
            none_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            small_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
            medium_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            plate_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            if(BackgroundMusicService.mReverb!=null)
            {
                System.out.println("aji mreverb not null");

                BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_SMALLROOM);
            }
        }
        else if(value==3)
        {
            none_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            small_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
            large_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            plate_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            if(BackgroundMusicService.mReverb!=null)
            {
                System.out.println("aji mreverb not null");

                BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_MEDIUMROOM);
            }
        }
        else if(value==4)
        {
            none_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            small_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
            medium_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            plate_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            if(BackgroundMusicService.mReverb!=null)
            {
                System.out.println("aji mreverb not null");

                BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_LARGEROOM);
            }
        }
        else if(value==5)
        {
            none_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            small_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
            large_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            plate_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            if(BackgroundMusicService.mReverb!=null)
            {
                System.out.println("aji mreverb not null");

                BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_MEDIUMHALL);
            }
        }
        else if(value==6)
        {
            none_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            small_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
            plate_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            if(BackgroundMusicService.mReverb!=null)
            {
                System.out.println("aji mreverb not null");

                BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
            }
        }
        else if(value==7)
        {
            none_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            small_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_room_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            medium_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            large_hall_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
            plate_equalizer.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
            if(BackgroundMusicService.mReverb!=null)
            {
                System.out.println("aji mreverb not null");

                BackgroundMusicService.mReverb.setPreset(PresetReverb.PRESET_PLATE);
            }
        }
      if(BackgroundMusicService.mReverb!=null)
      {
          System.out.println("aji mreverb not null");



          BackgroundMusicService.mReverb.setEnabled(true);
          BackgroundMusicService.bg_mediaPlayer.attachAuxEffect(BackgroundMusicService.mReverb.getId());
          BackgroundMusicService.bg_mediaPlayer.setAuxEffectSendLevel(1.0f);
      }
    }

//    @Subscribe
//    public void onEvent(EqualizerSeekbarModel equalizerSeekbarModel)
//    {
//
//        seekBarFinal[equalizerSeekbarModel.getPresetPos()].setProgress(BackgroundMusicService.mequalizer.getBandLevel(equalizerSeekbarModel.getPresetPos()) - equalizerSeekbarModel.getLowerEqualizerBandLevel());
//
//    };
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);//Register
//    }

    @Override
    public void onStop() {
        super.onStop();
        saveEqualizerSettings();
       // EventBus.getDefault().unregister(this);//unregister
    }
   public void entryEqualizerselected(int position)
   {
       switch(position) {
           case 0:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               break;
           case 1:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 2:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 3:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 4:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 5:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 6:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 7:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 8:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 9:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
           case 10:
               normal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               classical_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               dance_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               flat_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               folk_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               heavy_metal_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               hip_hop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               jazz_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               pop_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               rock_layout.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_yellow));
               custom_layout.setBackground(getResources().getDrawable(R.drawable.rounded_stroke_background));
               break;
       }
   }
    @Override
    public void onResume() {
        super.onResume();
//        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(myReceiver, filter);

    }
    @Override public void onPause() {
       // unregisterReceiver(myReceiver);
        super.onPause();
    }


}
