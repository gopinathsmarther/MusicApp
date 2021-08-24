package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

public class Settings extends AppCompatActivity {
    LinearLayout settings_background;
    LinearLayout settings_display;
    LinearLayout settings_audio;
    LinearLayout settings_headset;
    LinearLayout settings_lockscreen;
    LinearLayout settings_advanced;
    LinearLayout settings_others;
    SessionManager sessionManager;
    LinearLayout navigate_back;
    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        navigate_back=findViewById(R.id.navigate_back);
        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sessionManager = new SessionManager(Settings.this);

        settings_background = findViewById(R.id.settings_background);
        settings_display = findViewById(R.id.settings_display);
        settings_audio = findViewById(R.id.settings_audio);
        settings_lockscreen = findViewById(R.id.settings_lockscreen);
        settings_headset = findViewById(R.id.settings_headset);
        settings_advanced = findViewById(R.id.settings_advanced);
        settings_others = findViewById(R.id.settings_others);
        myReceiver = new MusicIntentReceiver();


        settings_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.intent_method(Settings.this,SettingsDisplayActivity.class);
            }
        });

        settings_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.intent_method(Settings.this,SettingsAudioActivity.class);
            }
        });

        settings_lockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.intent_method(Settings.this,SettingsLockscreenActivity.class);
            }
        });

        settings_headset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.intent_method(Settings.this,SettingsHeadsetActivity.class);
            }
        });

        settings_advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.intent_method(Settings.this,SettingsAdvancedActivity.class);
            }
        });

        settings_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.intent_method(Settings.this,SettingsOthersActivity.class);
            }
        });


        if (sessionManager.getIsDefaultThemeSelected()) {
            settings_background.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        } else {
            Drawable bg;
            Uri selectedImageUri = Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(Settings.this, selectedImageUri));
                bg = Drawable.createFromPath(f.getAbsolutePath());


//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
            } catch (Exception e) {
                bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
                e.printStackTrace();
            }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
            settings_background.setBackground(bg);
        }


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
